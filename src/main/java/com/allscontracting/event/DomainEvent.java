package com.allscontracting.event;

import java.util.Date;

public interface DomainEvent {
	String getObjectName();
	String getObjectId();
	EventType getEventType();
	Date getEventTime();
	String getMessage();
}
