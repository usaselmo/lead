package com.allscontracting.dto;

import java.util.List;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class MailDTO {

	public MailDTO(@NonNull List<PersonDTO> to, @NonNull List<PersonDTO> bcc, @NonNull String text, @NonNull List<MediaDTO> attachments, 
			@NonNull String type, @NonNull String subject) {
		super();
		this.to = to;
		this.bcc = bcc;
		this.text = text;
		this.attachments = attachments;
		this.type = type;
		this.subject = subject;
	}

	private final List<PersonDTO> to;
	private final List<PersonDTO> bcc;
	private final String text;
	private final List<MediaDTO> attachments;
	private final String type;
	private final String subject;

}
