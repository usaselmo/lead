package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.allscontracting.dto.MailDTO;

public interface EmailProvider {

	MailSender email(MailDTO mailDTO, List<File> attachments) throws IOException;

}