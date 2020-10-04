package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class CantReachMailProvider implements MailProvider {

	@Override
	public MailSender getMailProvider(Mail mail, Object... obj) throws IOException {

		MailSender mailSender = new MailSender(mail.getTo().stream().map(to -> to.getEmail()).collect(Collectors.toList()),
		    mail.getBcc().stream().map(to -> to.getEmail()).collect(Collectors.toList()), "We are trying to reach you for your job", getCantReachText(mail), null);
		return mailSender;
	}

	private String getCantReachText(Mail mailDTO) throws IOException {
		File file = getFile("templates/email/cantreach.html");
		String string = new String(Files.readAllBytes(file.toPath()));
		string = string.replace("{person.name}", mailDTO.getTo().get(0).getName());
		string = string.replace("{additionalText}", mailDTO.getText());
		return string;
	}

}
