package com.allscontracting.event;

import java.util.stream.Stream;

public enum EventType {
	
	BEGIN("Started", "Begin"),
	CONTACT_QUALIFY("Contact and Qualify", "Contact Client"),
	ASSIGN_TO_ESTIMATOR("Assign to Estimator", "Assign to Estimator"),
	SCHEDULE_VISIT("Visit Scheduled", "Schedule a Visit"),
	MARK_AS_VISITED("Marked as Visited", "Mark as Visited"),
	CREATE_PROPOSAL("Proposal Created", "Create Proposal"),
	SEND_PROPOSAL("Proposal Sent", "Send Proposal"),
	BEGIN_WORK("Work Started", "Begin Work"),
	FINISH_WORK("Work Finished", "Finish Work"),
	END_LEAD("Ended", "End Lead"),
	LOAD_VENDOR_FILE("Vendor File Loaded", "Load Vendor File");
	
	private String status;
	private String action;
	
	EventType(String status, String action) {
		this.status = status;
		this.action = action;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getAction() {
		return action;
	}

	public static EventType reverse(String description) {
		return Stream.of(EventType.values()).filter(type->type.toString().equalsIgnoreCase(description)).findFirst().get();
	}
	
}
