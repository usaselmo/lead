package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.service.Converter;

@Component
public class InvitationToBidMailProvider extends AbstractMailProvider {

	private static final String FILE_LOCATION_NAME = "templates/email/invitation-to-bid.html";
	private static final String TEMP_FILE_SUFFIX = "";
	private static final String TEMP_FILE_PREFIX = "leadsdc-";
	private static final String SUBJECT = "REQUEST FOR PROPOSAL - ";

	public MailSender email(MailDTO mailDTO, List<File> attachments, Invitation invitation) throws LeadsException {
		return new MailSender(mailDTO.getTo().stream().map(PersonDTO::getEmail).collect(Collectors.toList()),
		    mailDTO.getBcc().stream().map(PersonDTO::getEmail).collect(Collectors.toList()), SUBJECT + invitation.getLead().getTitle(),
		    this.getInvitationText(invitation), this.getFiles(mailDTO, attachments), getGmailPassword(), getGmailUser());
	}

	private List<File> getFiles(MailDTO mailDTO, List<File> attachments) {
		List<File> files = mailDTO.getAttachments().stream().map(att -> {
			try {
				return Files.write(Files.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX), att.getContent()).toFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		files.addAll(attachments);
		return files;
	}

	private String getInvitationText(Invitation invitation) throws LeadsException {
		try {
			File file = getFile(FILE_LOCATION_NAME);
			String string = new String(Files.readAllBytes(file.toPath()));
			string = string.replace("{companyName}", invitation.getCompany().getName());
			string = string.replace("{companyAddress}", StringUtils.isNotBlank(invitation.getCompany().getAddress()) ? invitation.getCompany().getAddress() : TEMP_FILE_SUFFIX);
			string = string.replace("{contactName}", invitation.getContact().getName());
			string = string.replace("{contactPhone}", invitation.getContact().getPhone());
			string = string.replace("{leadDescription}", invitation.getLead().getDescription());
			string = string.replace("{leadTitle}", invitation.getLead().getTitle());
			string = string.replace("{leadAddress}", invitation.getLead().getAddress());
			string = string.replace("{leadDueDate}", Converter.dateToString(invitation.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LeadsException("Could not fill e-mail template.");
		}
	}

}
