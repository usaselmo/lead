package com.allscontracting.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Media;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.MediaRepo;
import com.allscontracting.repo.ProposalRepository;
import com.allscontracting.service.mail.ProposalMailProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalService {

	private static final String PROPOSAL_FILE_NAME = "estimate";
	private final ProposalRepository proposalRepository;
	private final LeadRepository leadRepository;
	private final ReportService reportService;
	private final LogService logService;
	private final MediaRepo mediaRepo;

	private final ProposalMailProvider proposalMailProvider;

	@Transactional
	public ProposalDTO save(ProposalDTO proposalDTO, String leadId, User user) throws LeadsException {
		Proposal proposal = ProposalDTO.toProposal(proposalDTO);
		proposal.setDate(new Date());
		Lead lead = this.leadRepository.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		proposal.setLead(lead);
		proposal.setTotal(proposal.getItems().stream().map(line -> line.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
		defineNumber(proposal, lead);
		defineItems(proposal);
		Proposal res = proposalRepository.save(proposal);
		return ProposalDTO.of(res);
	}

	@Transactional
	public ProposalDTO update(ProposalDTO proposalDTO, String leadId, User user) throws LeadsException {
		proposalRepository.deleteById(Long.valueOf(proposalDTO.getId()));
		return this.save(proposalDTO, leadId, user);
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
			Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
			HashMap<String, Object> map = getProposalParameters(proposal, person);
			String streamFileName = getProposalFileName(proposal, person, "pdf");
			this.reportService.getReportAsPdfStream(response, map, streamFileName, PROPOSAL_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getProposalAsRtfStream(HttpServletResponse response, String proposalId)
	    throws IOException, JRException, SQLException, NumberFormatException, LeadsException {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getCompany();
		HashMap<String, Object> map = getProposalParameters(proposal, person);
		String streamFileName = getProposalFileName(proposal, person, "rtf");
		this.reportService.getReportAsRtfStream(response, map, streamFileName, PROPOSAL_FILE_NAME);
	}

	@Transactional
	public void sendPdfByEmail(long proposalId, User user, MailDTO mailDTO, List<File> attachments) throws Exception {
		Proposal proposal = this.proposalRepository.findById(Long.valueOf(proposalId)).orElseThrow(() -> new LeadsException("Proposal not found"));
		Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
		this.proposalMailProvider
			.email(mailDTO, attachments, proposal)
			.onError((error) -> {
				log.error("Error sending mail: " + error);
			})
			.onSuccess(() -> {
				proposal.getLead().setEvent(Event.SEND_PROPOSAL);
				proposal.setEmailed(true);
				this.proposalRepository.save(proposal);
				logService.event(Lead.class, proposal.getLead().getId(), Event.EMAIL_SENT, user, "Proposal E-mailed to " + person.getName() + ". Proposal # "
				    + proposal.getNumber() + " (" + NumberFormat.getCurrencyInstance().format(proposal.getTotal()) + ")");
			})
			.send();
	}

	public void getMediaAsPdfStream(Long mediaId, HttpServletResponse response) throws LeadsException, IOException {
		Media media = this.mediaRepo.findById(mediaId).orElseThrow(() -> new LeadsException("Could not find Media"));
		this.reportService.getFileAsStream(response, media.getName(), media.getContent(), media.getType());
	}

	@Transactional
	public ProposalDTO markAsEmailed(long proposalId, User user) throws LeadsException {
		Proposal proposal = this.proposalRepository.findById(proposalId).orElseThrow(() -> new LeadsException("Could not find Proposal"));
		proposal.setEmailed(!proposal.isEmailed());
		this.proposalRepository.save(proposal);
		
		if(proposal.isEmailed()) {
			Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
			proposal.getLead().setEvent(Event.SEND_PROPOSAL);
			logService.event(Lead.class, proposal.getLead().getId(), Event.EMAIL_SENT, user, "Proposal E-mailed to " + person.getName() + ". Proposal # "
			    + proposal.getNumber() + " (" + NumberFormat.getCurrencyInstance().format(proposal.getTotal()) + ")");
		}

		return ProposalDTO.of(proposal);
	}

	@Transactional
	public ProposalDTO markAsAccepted(long proposalId, User user) throws LeadsException {
		Proposal proposal = this.proposalRepository.findById(proposalId).orElseThrow(() -> new LeadsException("Could not find Proposal"));
		proposal.setAccepted(!proposal.isAccepted());
		this.proposalRepository.save(proposal);
		
		logService.event(Lead.class, proposal.getLead().getId(), Event.PROPOSAL_ACCEPTED, user, "Proposal marked as "+ proposal.isAccepted() +" Accepted. Proposal # "
		    + proposal.getNumber() + " (" + NumberFormat.getCurrencyInstance().format(proposal.getTotal()) + ")");
		
		return ProposalDTO.of(proposal);
	}

	public static String getProposalFileName(Proposal proposal, Client client, String suffix) {
		StringBuilder streamFileName = new StringBuilder(client.getName()).append(" - ")
		    .append(!StringUtils.isEmpty(proposal.getLead().getAddress()) ? proposal.getLead().getAddress() : client.getAddress()).append(" - ")
		    .append(
		        proposal.isChangeorder() ? "Change Order " + Converter.dateToString(proposal.getDate(), Converter.MM_dd_yy) : "Proposal #" + proposal.getNumber())
		    .append("." + suffix);
		return streamFileName.toString();
	}

	public static HashMap<String, Object> getProposalParameters(Proposal proposal, Client client) {
		HashMap<String, Object> map = new HashMap<>();
		new StringBuilder().append(!StringUtils.isEmpty(proposal.getNote()) ? proposal.getNote() : "")
		    .append(proposal.isCallMissUtility() && !StringUtils.isEmpty(proposal.getNote()) ? "\r\n" : "")
		    .append(proposal.isCallMissUtility() ? "We will call Miss Utility" : "").toString();
		map.put("CLIENT", client);
		map.put("ESTIMATOR",
		    proposal.getLead() != null && proposal.getLead().getEstimator() != null && !StringUtils.isEmpty(proposal.getLead().getEstimator().getName())
		        ? proposal.getLead().getEstimator().getName()
		        : "Eddie Lopes");
		map.put("PROPOSAL", ProposalDTO.of(proposal));
		map.put("PROPOSAL_ID", proposal.getId());
		map.put("LEAD", LeadDTO.of(proposal.getLead()));
		return map;
	}

	private void defineItems(Proposal proposal) {
		proposal.getItems().forEach(item -> {
			proposal.addItem(item);
			item.getLines().forEach(line -> item.addLine(line));
		});
	}

	private void defineNumber(Proposal proposal, Lead lead) {
		if (proposal.getNumber() == null) {
			if (lead.getProposals() != null && lead.getProposals().size() > 0) {
				long max = 0;
				if (proposal.isChangeorder())
					max = lead.getProposals().stream().filter(p -> p.isChangeorder()).mapToLong(p -> p.getNumber()).max().orElse(0L);
				else
					max = lead.getProposals().stream().mapToLong(p -> p.getNumber()).max().orElse(0L);
				proposal.setNumber(max + 1);
			} else {
				proposal.setNumber(1L);
			}
		}
	}

}
