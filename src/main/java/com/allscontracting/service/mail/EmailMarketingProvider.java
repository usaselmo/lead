package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;

public class EmailMarketingProvider extends AbstractMailProvider implements EmailProvider{

	private static final String FILE_LOCATION_NAME = "templates/email/email-marketing.html";
	private static final String SUBJECT = "Nice concrete Patios and Driveways";

	@Override
	public MailSender email(MailDTO mailDTO, List<File> attachments) throws IOException {
		
		MailSender mailSender = new MailSender(
				mailDTO.getTo() .stream().map(PersonDTO::getEmail).collect(Collectors.toList()),
		    mailDTO.getBcc().stream().map(PersonDTO::getEmail).collect(Collectors.toList()), 
		    SUBJECT, 
		    getCantReachText(mailDTO),
		    attachments
		);
		return mailSender;
	}

	private String getCantReachText(MailDTO mailDTO) throws IOException {
		File file = getFile(FILE_LOCATION_NAME);
		String string = new String(Files.readAllBytes(file.toPath()));
		return string;
	}

}
