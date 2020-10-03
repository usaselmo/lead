package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class InvitationToBidMailProvider implements MailProvider {

	@Override
	public MailSender getMailProvider(Mail mail, Object... obj) throws IOException, LeadsException {
		final Invitation invitation = (Invitation) obj[0];
		MailSender mailSender = new MailSender(
				mail.getTo().stream().map(to->to.getEmail()).collect(Collectors.toList()),
				mail.getBcc().stream().map(to->to.getEmail()).collect(Collectors.toList()),
				"REQUEST FOR PROPOSAL - " + invitation.getLead().getTitle(), 
				this.getInvitationText(invitation),
				this.getFiles(mail));
		return mailSender;
	}

	private List<File> getFiles(Mail mail) {
		return mail.getAttachments().stream().map(att -> {
			try {
				return Files.write(Files.createTempFile("leadsdc-", ""), att.getContent()).toFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	private String getInvitationText(Invitation invitation) throws LeadsException {
		try {
			File file = getFile("templates/email/invitation-to-bid.html");
			String string = new String(Files.readAllBytes(file.toPath()));
			string = string.replace("{companyName}", invitation.getCompany().getName());
			string = string.replace("{companyAddress}",
					StringUtils.isNotBlank(invitation.getCompany().getAddress()) ? invitation.getCompany().getAddress()
							: "");
			string = string.replace("{contactName}", invitation.getContact().getName());
			string = string.replace("{contactPhone}", invitation.getContact().getPhone());
			string = string.replace("{leadDescription}", invitation.getLead().getDescription());
			string = string.replace("{leadTitle}", invitation.getLead().getTitle());
			string = string.replace("{leadAddress}", invitation.getLead().getAddress());
			string = string.replace("{leadDueDate}",
					Converter.dateToString(invitation.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LeadsException("Could not fill e-mail template.");
		}
	}

}
