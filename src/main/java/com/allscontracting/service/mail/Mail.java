package com.allscontracting.service.mail;

import java.util.List;

import com.allscontracting.model.Media;
import com.allscontracting.model.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Mail {
	private final List<Person> to;
	private final List<Person> bcc;
	private final String text;
	private final List<Media> attachments;
	private final TYPE type;
	private final String subject;

	public static enum TYPE {
		CANT_REACH, HIRING_DECISION, PROPOSAL, INVITATION_TO_BID
	}
}
