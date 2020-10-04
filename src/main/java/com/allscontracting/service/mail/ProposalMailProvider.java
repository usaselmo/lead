package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.allscontracting.model.Client;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.ProposalService;
import com.allscontracting.service.ReportService;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;

@AllArgsConstructor
@Component
public class ProposalMailProvider implements MailProvider {

	private final ReportService reportService;
	private final ProposalService proposalService;

	private static final String PROPOSAL_FILE_NAME = "estimate";

	@Override
	public MailSender getMailProvider(Mail mail, Object... obj) throws IOException, JRException, SQLException {
		final Proposal proposal = (Proposal) obj[0];
		Client person = proposal.getLead().getClient() != null ? proposal.getLead().getClient() : proposal.getLead().getContact();
		HashMap<String, Object> map = this.proposalService.getProposalParameters(proposal, person);
		String streamFileName = this.proposalService.getProposalFileName(proposal, person, "pdf");
		File file = reportService.getReportAsPdfFile(streamFileName, map, PROPOSAL_FILE_NAME);

		MailSender mailSender = new MailSender(mail.getTo().stream().map(to -> to.getEmail()).collect(Collectors.toList()),
		    mail.getBcc().stream().map(to -> to.getEmail()).collect(Collectors.toList()), "Your Proposal from All's Contracting",
		    this.getProposalText(proposal, mail), Arrays.asList(file));
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

}
