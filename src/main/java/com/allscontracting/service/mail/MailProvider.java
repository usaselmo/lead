package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.allscontracting.dto.MailDTO;

public interface MailProvider {

	MailSender email(MailDTO mailDTO, List<File> attachments, Object... obj) throws Exception;

	default File getFile(String fileName) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Path tempFile = Files.createTempFile("", "");
		Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
		IOUtils.closeQuietly(is);
		return tempFile.toFile();
	}

}
