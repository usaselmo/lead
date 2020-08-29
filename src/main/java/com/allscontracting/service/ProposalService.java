package com.allscontracting.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.ItemRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.LineRepository;
import com.allscontracting.repo.ProposalRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Service
public class ProposalService {

	private static final String PROPOSAL_FILE_NAME = "estimate";
	@Autowired
	ProposalRepository proposalRepository;
	@Autowired
	LeadRepository leadRepository;
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	LineRepository lineRepository;
	@Autowired
	ReportService reportService;
	@Autowired
	MailService mailService;
	@Autowired
	LogService logService;

	@Transactional
	public ProposalDTO save(ProposalDTO proposalDTO, String leadId, Long userId) throws LeadsException {
		Proposal proposal = ProposalDTO.toProposal(proposalDTO);
		proposal.setDate(new Date());
		Lead lead = this.leadRepository.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		proposal.setLead(lead);
		if (proposal.getNumber() == null) {
			long number = lead.getProposals().size();
			proposal.setNumber(number + 1);
		}
		if (StringUtils.isEmpty(proposalDTO.getTotal()) || new BigDecimal(proposalDTO.getTotal()).equals(BigDecimal.ZERO)) {
			proposal.setTotal(proposal.getItems().stream().map(line -> line.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
		}
		proposal.getItems().forEach(item -> {
			proposal.addItem(item);
			item.getLines().forEach(line -> item.addLine(line));
		});
		Proposal res = proposalRepository.save(proposal);
		return ProposalDTO.of(res);
	}

	@Transactional
	public void delete(String leadId, String proposalId, Long userId) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepository.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		lead.removeProposal(proposal);
		this.leadRepository.save(lead);
		this.proposalRepository.delete(proposal);
	}

	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws Exception {
		try {
			Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
			Client person = proposal.getLead().getClient()!=null?proposal.getLead().getClient():proposal.getLead().getCompany();
			HashMap<String, Object> map = getProposalParameters(proposal, person);
			String streamFileName = getProposalFileName(proposal, person, "pdf");
			this.reportService.getReportAsPdfStream(response, map, streamFileName, PROPOSAL_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getProposalAsRtfStream(HttpServletResponse response, String proposalId) throws IOException, JRException, SQLException, NumberFormatException, LeadsException {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		Client person = proposal.getLead().getClient()!=null?proposal.getLead().getClient():proposal.getLead().getCompany();
		HashMap<String, Object> map = getProposalParameters(proposal, person);
		String streamFileName = getProposalFileName(proposal, person, "rtf");
		this.reportService.getReportAsRtfStream(response, map, streamFileName, PROPOSAL_FILE_NAME);
	}

	@Transactional
	public void sendPdfByEmail(long proposalId, User user) throws JRException, SQLException, IOException, LeadsException {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		Client person = proposal.getLead().getClient()!=null?proposal.getLead().getClient():proposal.getLead().getCompany();
		HashMap<String, Object> map = getProposalParameters(proposal, person);
		String streamFileName = getProposalFileName(proposal, person, "pdf");
		File res = reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);
		this.mailService.sendProposalByEmail(proposal, person, res)
			.onError((error) -> {
				log.error("Error sending mail");
			})
			.onSuccess(()->{
				proposal.getLead().setEvent(Event.SEND_PROPOSAL);
				proposal.setEmailed(true);
				this.proposalRepository.save(proposal);
				logService.event(Lead.class, proposal.getLead().getId(), Event.EMAIL_SENT, user, "Proposal E-mailed to " + person.getName() + ". Proposal # " + proposal.getNumber() + " (" + NumberFormat.getCurrencyInstance().format(proposal.getTotal()) + ")");
			})
			.send(); 
	}

	private String getProposalFileName(Proposal proposal, Client person, String suffix) {
		String streamFileName = new StringBuilder(person.getName()).append(" - ").append(person.getAddress()).append(" - ").append("proposal #").append(proposal.getNumber()).append("." + suffix).toString();
		return streamFileName;
	}

	private HashMap<String, Object> getProposalParameters(Proposal proposal, Client client) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("ESTIMATOR", proposal.getLead()!=null && proposal.getLead().getEstimator()!=null && !StringUtils.isEmpty(proposal.getLead().getEstimator().getName()) ?proposal.getLead().getEstimator().getName():"Eddie Lopes");
		map.put("PROPOSAL", ProposalDTO.of(proposal));
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}
	
}
