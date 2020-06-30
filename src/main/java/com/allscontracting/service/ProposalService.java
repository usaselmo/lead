package com.allscontracting.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.LeadStatusChangeEvent;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ItemRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.LineRepository;
import com.allscontracting.repo.ProposalRepository;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Service
public class ProposalService {
	
	private static final String PROPOSAL_FILE_NAME = "proposal2";
	@Autowired ProposalRepository proposalRepository;
	@Autowired LeadRepository leadRepository;
	@Autowired ItemRepository itemRepository;
	@Autowired LineRepository lineRepository;
	@Autowired ReportService reportService;
	@Autowired MailService mailService;
	@Autowired EventManager eventManager;
	
	@Transactional
	public ProposalDTO save(ProposalDTO proposalDTO, String leadId, Long userId) throws LeadsException {
		Proposal proposal = ProposalDTO.toProposal(proposalDTO);
		Lead lead = this.leadRepository.findById(leadId).orElseThrow(()->new LeadsException("Lead not found"));
		proposal.setLead(lead);
		if(proposal.getNumber()==null) {
			long number = lead.getProposals().size();
			proposal.setNumber(number+1);
		}
		proposal.setTotal(proposal.getItems().stream().map(line->line.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
		proposal.getItems().forEach(item-> {
			proposal.addItem(item);
			item.getLines().forEach(line->item.addLine(line)); 
		});
		Proposal res = proposalRepository.save(proposal);
		return ProposalDTO.toDTO(res);
	}

	@Transactional
	public void delete(String leadId, String proposalId, Long userId) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepository.findById(leadId).orElseThrow(()->new LeadsException("Lead not found"));
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(()->new LeadsException("Proposal not found"));
		lead.removeProposal(proposal);
		this.leadRepository.save(lead);
		this.proposalRepository.delete(proposal);
	}

	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws Exception {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId )).orElseThrow(()->new LeadsException("Proposal not found"));
		Client client = proposal.getLead().getClient();
		HashMap<String, Object> map = getProposalParameters(proposal, client);
		String streamFileName = getProposalFileName(proposal, client, "pdf");
		this.reportService.getReportAsPdfStream(response, map, streamFileName, PROPOSAL_FILE_NAME); 		
	}

	public void getProposalAsRtfStream(HttpServletResponse response, String proposalId) throws IOException, JRException, SQLException, NumberFormatException, LeadsException {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId )).orElseThrow(()->new LeadsException("Proposal not found"));
		Client client = proposal.getLead().getClient();
		HashMap<String, Object> map = getProposalParameters(proposal, client);
		String streamFileName = getProposalFileName(proposal, client, "rtf");
		this.reportService.getReportAsRtfStream(response, map, streamFileName, PROPOSAL_FILE_NAME); 		
	}

	@Transactional
	public void sendPdfByEmail(long proposalId, Long userId) throws JRException, SQLException, IOException, LeadsException {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(()->new LeadsException("Proposal not found"));
		Client client = proposal.getLead().getClient();
		HashMap<String, Object> map = getProposalParameters(proposal, client);
		String streamFileName = getProposalFileName(proposal, client, "pdf");
		File res = reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);
		this.mailService.sendProposalByEmail(proposal, client, res).onError((error) -> log.error("Error sending mail")).send();
		proposal.getLead().setEvent(EventType.SEND_PROPOSAL);
		proposal.setEmailed(true);
		this.proposalRepository.save(proposal);
		this.eventManager.notifyAllListeners(new LeadStatusChangeEvent(EventType.SEND_PROPOSAL.toString(), proposal.getLead().getId(), userId));
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), proposal.getLead().getId(), "Proposal E-mailed to " + client.getName() + ". Proposal # " + proposal.getNumber() + " (" + NumberFormat.getCurrencyInstance().format(proposal.getTotal()) + ")", userId));
	}

	private String getProposalFileName(Proposal proposal, Client client, String suffix) {
		String streamFileName = new StringBuilder(client.getName()).append(" - ").append(client.getAddress()).append(" - ").append("proposal #").append(proposal.getNumber()).append("."+suffix).toString();
		return streamFileName;
	}

	private HashMap<String, Object> getProposalParameters(Proposal proposal, Client client) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}

}
