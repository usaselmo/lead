package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public abstract class AbstractMailProvider {

	@Value("${gmail-password}")
	protected String gmailPassword;

	@Value("${gmail-user}")
	protected String gmailUser;

	File getFile(String fileName) throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Path tempFile = Files.createTempFile("", "");
		assert is != null;
		Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
		IOUtils.closeQuietly(is);
		return tempFile.toFile();
	}

}
