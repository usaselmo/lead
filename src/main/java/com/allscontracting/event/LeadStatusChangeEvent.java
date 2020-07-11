package com.allscontracting.event;

import java.util.Date;

import com.allscontracting.model.Lead;
import com.allscontracting.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class LeadStatusChangeEvent implements DomainEvent {

	private final String objectName = Lead.class.getSimpleName();
	private final Date eventTime = new Date();
	private String oid;
	private String evTp;
	private User user;

	@Override
	public String getEventType() {
		return this.evTp;
	}

	@Override
	public String getObjectId() {
		return this.oid;
	}

	@Override
	public String getMessage() {
		return null;
	}

}
