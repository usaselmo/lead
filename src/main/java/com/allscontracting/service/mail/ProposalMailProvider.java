package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.ProposalService;
import com.allscontracting.service.ReportService;
import com.allscontracting.type.Client;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Component
public class ProposalMailProvider extends AbstractMailProvider {

	private final ReportService reportService;
	private static final String PROPOSAL_FILE_NAME = "estimate";

	public MailSender email(MailDTO mailDTO, List<File> attachments, Proposal proposal) throws IOException, JRException, SQLException {
		attachments.add(getFile(proposal));
		return new MailSender(mailDTO.getTo().stream().map(PersonDTO::getEmail).collect(Collectors.toList()),
		    mailDTO.getBcc().stream().map(PersonDTO::getEmail).collect(Collectors.toList()), "Your Proposal from All's Contracting",
		    this.getProposalText(proposal, mailDTO), attachments, getGmailPassword(), getGmailUser());
	}

	private File getFile(final Proposal proposal) throws JRException, SQLException, IOException {
		Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
		HashMap<String, Object> map = ProposalService.getProposalParameters(proposal, person);
		String streamFileName = ProposalService.getProposalFileName(proposal, person, "pdf");
		return reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);
	}

	private String getProposalText(Proposal proposal, MailDTO mailDTO) throws IOException {
		File file = getFile("templates/email/proposal.html");
		String string = new String(Files.readAllBytes(file.toPath()));
		string = string.replace("{person.name}", mailDTO.getTo().get(0).getName());
		string = string.replace("{number}", String.valueOf(proposal.getNumber()));
		string = string.replace("{additionalText}", mailDTO.getText());
		return string;
	}

}
