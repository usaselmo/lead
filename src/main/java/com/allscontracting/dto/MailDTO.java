package com.allscontracting.dto;

import java.util.List;

import com.allscontracting.service.mail.Mail;

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

	public static MailDTO of(Mail mail) {
		return MailDTO.builder().attachments(MediaDTO.of(mail.getAttachments())).bcc(PersonDTO.of(mail.getBcc()))
				.subject(mail.getSubject()).text(mail.getText()).to(PersonDTO.of(mail.getTo()))
				.type(mail.getType().name()).build();
	}

	public static Mail to(MailDTO dto) {
		if (dto == null)
			return null;
		Mail mail = new Mail(
				PersonDTO.toPerson(dto.getTo()), 
				PersonDTO.toPerson(dto.getBcc()), 
				dto.getText(),
				MediaDTO.toMedia(dto.getAttachments()), 
				dto.getType()==null?null:Mail.TYPE.valueOf(dto.getType()), 
				dto.getSubject());
		return mail;
	}

}
