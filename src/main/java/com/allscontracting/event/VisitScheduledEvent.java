package com.allscontracting.event;

import java.util.Date;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class VisitScheduledEvent implements DomainEvent {

	private Lead lead;
	private Client client;
	private Date visitSchedule;
	private Long userId;
	private final String eventType = EventType.SCHEDULE_VISIT.toString();
	private final Date eventTime = new Date();
	private final String objectName = Lead.class.getSimpleName();
	
	@Override
	public String getObjectId() {
		return String.valueOf(lead.getId());
	}

	@Override
	public String getMessage() {
		return null;
	}

}
