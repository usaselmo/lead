package com.allscontracting.event;

import java.util.stream.Stream;

public enum EventType {
	
	BEGIN("Started", "Begin", "BE"),
	CONTACT_QUALIFY("Contact and Qualify", "Contact Client", "CQ"),
	ASSIGN_TO_ESTIMATOR("Assign to Estimator", "Assign to Estimator", "AE"),
	SCHEDULE_VISIT("Visit Scheduled", "Schedule a Visit", "SV"),
	MARK_AS_VISITED("Marked as Visited", "Mark as Visited", "MV"),
	CREATE_PROPOSAL("Proposal Created", "Create Proposal", "CP"),
	SEND_PROPOSAL("Proposal Sent", "Send Proposal", "SP"),
	BEGIN_WORK("Work Started", "Begin Work", "BW"),
	FINISH_WORK("Work Finished", "Finish Work", "FW"),
	END_LEAD("Ended", "End Lead", "EL"),
	LOAD_VENDOR_FILE("Vendor File Loaded", "Load Vendor File", "LVF");
	
	private String status;
	private String action;
	private String abbreviation;
	
	EventType(String status, String action, String abbreviation) {
		this.status = status;
		this.action = action;
		this.abbreviation = abbreviation;
	}
	
	public String getAbbreviation() {
		return abbreviation;
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
