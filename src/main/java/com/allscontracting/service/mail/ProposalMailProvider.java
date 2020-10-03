package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.model.Client;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.Converter;
import com.allscontracting.service.ReportService;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@AllArgsConstructor
@Component
public class ProposalMailProvider implements MailProvider {

	//private final Proposal proposal;
	private final ReportService reportService;

	private static final String PROPOSAL_FILE_NAME = "estimate";

	@Override
	public MailSender getMailProvider(Mail mail, Object... obj) throws IOException, JRException, SQLException {
		final Proposal proposal = (Proposal) obj[0];
		Client person = proposal.getLead().getClient()!=null?proposal.getLead().getClient():proposal.getLead().getContact();
		HashMap<String, Object> map = getProposalParameters(proposal, person);
		String streamFileName = getProposalFileName(proposal, person, "pdf");
		File file = reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);
		
		MailSender mailSender = new MailSender(
				mail.getTo().stream().map(to->to.getEmail()).collect(Collectors.toList()),
				mail.getBcc().stream().map(to->to.getEmail()).collect(Collectors.toList()),
				"Your Proposal from All's Contracting", 
				this.getProposalText(proposal, mail),
				Arrays.asList(file)
				);
		return mailSender; 
	}

	private String getProposalText(Proposal proposal, Mail mailDTO) throws IOException {
		File file = getFile("templates/email/proposal.html");
		String string = new String(Files.readAllBytes(file.toPath()));
		string = string.replace("{person.name}", mailDTO.getTo().get(0).getName());
		string = string.replace("{number}", String.valueOf(proposal.getNumber()));
		string = string.replace("{additionalText}", mailDTO.getText());
		return string;
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

	private String getProposalFileName(Proposal proposal, Client client, String suffix) {
		StringBuilder streamFileName = new StringBuilder(client.getName())
				.append(" - ")
				.append( !StringUtils.isEmpty(proposal.getLead().getAddress())?proposal.getLead().getAddress():client.getAddress() ).append(" - ")
				.append(proposal.isChangeorder()? "Change Order "+Converter.dateToString(proposal.getDate(), Converter.MM_dd_yy) : "Proposal #"+proposal.getNumber())
				.append("." + suffix);
		return streamFileName.toString();
	}
	
}
