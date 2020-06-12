package com.allscontracting.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AuditEvent implements DomainEvent{

	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObjectId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventType getEventType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
