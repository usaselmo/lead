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

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.LeadDTO;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {

	private static final String PROPOSAL_FILE_NAME = "estimate";
	public final ProposalRepository proposalRepository;
	public final LeadRepository leadRepository;
	public final ItemRepository itemRepository;
	public final LineRepository lineRepository;
	public final ReportService reportService;
	public final MailService mailService;
	public final LogService logService;

	@Transactional
	public ProposalDTO save(ProposalDTO proposalDTO, String leadId, User user) throws LeadsException {
		Proposal proposal = ProposalDTO.toProposal(proposalDTO);
		proposal.setDate(new Date());
		Lead lead = this.leadRepository.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		proposal.setLead(lead);
		defineNumber(proposal, lead);
		defineTotal(proposalDTO, proposal);
		defineItems(proposal);
		Proposal res = proposalRepository.save(proposal);
		return ProposalDTO.of(res);
	}

	@Transactional
	public ProposalDTO update(ProposalDTO proposalDTO, String leadId, User user) throws LeadsException{
		proposalRepository.deleteById(Long.valueOf(proposalDTO.getId()));
		return this.save(proposalDTO, leadId, user);
	}

	private void defineItems(Proposal proposal) {
		proposal.getItems().forEach(item -> {
			proposal.addItem(item);
			item.getLines().forEach(line -> item.addLine(line));
		});
	}

	private void defineNumber(Proposal proposal, Lead lead) {
		if (proposal.getNumber() == null) {
			if(lead.getProposals()!=null && lead.getProposals().size()>0) {
				long max = lead.getProposals().stream().mapToLong(p->p.getNumber()).max().orElse(0L);
				proposal.setNumber(max+1);
			}else {
				proposal.setNumber(1L); 
			}
		}
	}

	private void defineTotal(ProposalDTO proposalDTO, Proposal proposal) {
		if (StringUtils.isEmpty(proposalDTO.getTotal()) || new BigDecimal(proposalDTO.getTotal()).equals(BigDecimal.ZERO)) {
			proposal.setTotal(proposal.getItems().stream().map(line -> line.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
		}
	}

	@Transactional
	public void delete(String leadId, String proposalId, User user) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepository.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		lead.removeProposal(proposal);
		this.leadRepository.save(lead);
		this.proposalRepository.delete(proposal);
		logService.event(Lead.class, lead.getId(), Event.DELETE, user);
	}

	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws Exception {
		try {
			Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
			Client person = proposal.getLead().getClient()!=null?proposal.getLead().getClient():proposal.getLead().getContact();
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
				log.error("Error sending mail: " + error);
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
		HashMap<String, Object> map = new HashMap<>(); new StringBuilder().append(!StringUtils.isEmpty(proposal.getNote())?proposal.getNote():"").append(proposal.isCallMissUtility()&&!StringUtils.isEmpty(proposal.getNote())?"\r\n":"").append(proposal.isCallMissUtility()?"We will call Miss Utility":"").toString();
		map.put("CLIENT", client);
		map.put("ESTIMATOR", proposal.getLead()!=null && proposal.getLead().getEstimator()!=null && !StringUtils.isEmpty(proposal.getLead().getEstimator().getName()) ?proposal.getLead().getEstimator().getName():"Eddie Lopes");
		map.put("PROPOSAL", ProposalDTO.of(proposal));
		map.put("PROPOSAL_ID", proposal.getId());
		map.put("LEAD", LeadDTO.of(proposal.getLead()));
		return map;
	}
	
}
