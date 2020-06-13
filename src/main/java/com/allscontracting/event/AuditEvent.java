package com.allscontracting.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AuditEvent implements DomainEvent{
	
	public static final String KEY = "AUDIT_EVENT";
	private String objectName ;
	private String objectId ;
	private EventType eventType;
	private String message;
	
}
