package com.allscontracting.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AuditEvent implements DomainEvent{
	
	private String objectName ;
	private String objectId ;
	private final String eventType = "AUDIT_EVENT";
	private String message;
	
}
