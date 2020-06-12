package com.allscontracting.event;

import java.util.Date;

import com.allscontracting.model.Lead;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LeadStatusChangeEvent implements DomainEvent {

	private final String objectName = Lead.class.getSimpleName();
	private final Date eventTime = new Date();
	private String oid;
	private EventType evTp;

	public LeadStatusChangeEvent(EventType eventType, String leadId) {
		super();
		this.oid = leadId;
		this.evTp = eventType;
	}

	@Override
	public EventType getEventType() {
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
