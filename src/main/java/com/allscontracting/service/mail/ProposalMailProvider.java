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
import com.allscontracting.model.Client;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.ProposalService;
import com.allscontracting.service.ReportService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@RequiredArgsConstructor
@Component
public class ProposalMailProvider implements MailProvider {

	private final ReportService reportService;
	private static final String PROPOSAL_FILE_NAME = "estimate";

	@Override
	public MailSender email(MailDTO mailDTO, List<File> attachments, Object... obj) throws IOException, JRException, SQLException {
		final Proposal proposal = (Proposal) obj[0];
		attachments.add(getFile(proposal));
		MailSender mailSender = new MailSender(mailDTO.getTo().stream().map(to -> to.getEmail()).collect(Collectors.toList()),
		    mailDTO.getBcc().stream().map(to -> to.getEmail()).collect(Collectors.toList()), "Your Proposal from All's Contracting",
		    this.getProposalText(proposal, mailDTO), attachments);
		return mailSender;
	}

	private File getFile(final Proposal proposal) throws JRException, SQLException, IOException {
		Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
		HashMap<String, Object> map = ProposalService.getProposalParameters(proposal, person);
		String streamFileName = ProposalService.getProposalFileName(proposal, person, "pdf");
		File file = reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);
		return file;
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
