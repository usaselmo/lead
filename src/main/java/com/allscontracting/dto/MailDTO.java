package com.allscontracting.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class MailDTO {
	private final List<PersonDTO> to;
	private final List<PersonDTO> bcc;
	private final String text;
	private final List<MediaDTO> attachments;
	private final String type; 
	private final String subject;

}
