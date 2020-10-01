package com.allscontracting.dto;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MailDTO {

	private String subject;
	private String mainText;
	private String additionalText;
	private String[] emailTo;
	private File[] attachmentFiles;
	
	
}
