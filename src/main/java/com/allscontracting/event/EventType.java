package com.allscontracting.event;

import java.util.stream.Stream;

public enum EventType {
	BEGIN("Started"), 
	CONTACT_QUALIFY("Contact and Qualify"), 
	ASSIGN_TO_ESTIMATOR("Assign to Estimator"), 
	SCHEDULE_VISIT("Visit scheduled"), 
	MARK_AS_VISITED("Marked as visited"),
	CREATE_PROPOSAL("Proposal created"),
	SEND_PROPOSAL("Proposal sent"), 
	BEGIN_WORK("Work started"), 
	FINISH_WORK("Work finished"), 
	END_LEAD("Done"), 
	LOAD_VENDOR_FILE("Vendor file loaded");
	
	private String status;
	
	EventType(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public static EventType reverse(String description) {
		return Stream.of(EventType.values()).filter(type->type.toString().equalsIgnoreCase(description)).findFirst().get();
	}
	
}
