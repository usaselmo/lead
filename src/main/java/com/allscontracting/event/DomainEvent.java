package com.allscontracting.event;

import java.util.Date;

public interface DomainEvent {
	String getObjectName();
	String getObjectId();
	String getEventType();
	String getMessage();
	default Date getEventTime() {
		return new Date();
	}
}
