package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;

@Component
public class HiringDecisionMailProvider implements MailProvider {

	private static final String FILE_LOCATION_NAME = "templates/email/hiring-decision.html";
	private static final String SUBJECT = "Have you made a decision ?";

	@Override
	public MailSender email(MailDTO mailDTO, List<File> attachments, Object... obj) throws IOException {
		MailSender mailSender = new MailSender(mailDTO.getTo().stream().map(PersonDTO::getEmail).collect(Collectors.toList()),
		    mailDTO.getBcc().stream().map(PersonDTO::getEmail).collect(Collectors.toList()), SUBJECT, getHiringDecisionText(mailDTO),
		    attachments);
		return mailSender;
	}

	private String getHiringDecisionText(MailDTO mailDTO) throws IOException {
		File file = getFile(FILE_LOCATION_NAME);
		String string = new String(Files.readAllBytes(file.toPath()));
		string = string.replace("{person.name}", mailDTO.getTo().get(0).getName());
		string = string.replace("{additionalText}", mailDTO.getText());
		return string;
	}

}
