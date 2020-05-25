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

	public LeadStatusChangeEvent(EventType evTp, String oid) {
		super();
		this.oid = oid;
		this.evTp = evTp;
	}

	@Override
	public EventType getEventType() {
		return this.evTp;
	}

	@Override
	public String getObjectId() {
		return this.oid;
	}

}
