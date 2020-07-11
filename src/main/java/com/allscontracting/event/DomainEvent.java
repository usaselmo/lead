package com.allscontracting.event;

import java.util.Date;

import com.allscontracting.model.User;

public interface DomainEvent {
	String getObjectName();
	String getObjectId();
	String getEventType();
	String getMessage();
	User getUser();
	
	default Date getEventTime() {
		return new Date();
	}
}
