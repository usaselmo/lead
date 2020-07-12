package com.allscontracting.event;

import java.util.stream.Stream;

public enum EventType {
	
	BEGIN("CONTACTING", "START OVER", "BE"),
	ASSIGN_TO_ESTIMATOR("ASSIGNED TO ESTIMATOR", "Assign to Estimator", "AE"),
	BID("BIDDING", "BID", "BD"),
	CREATE_PROPOSAL("PROPOSAL CREATED", "Create Proposal", "CP"),
	SEND_PROPOSAL("PROPOSAL SENT", "Send Proposal", "SP"),
	ACCEPT_PROPOSAL("PROPOSAL ACCEPTED", "Accept Proposal", "AP"),
	BEGIN_WORK("WORK IN PROGRESS", "Begin Work", "BW"),
	FINISH_WORK("WORK FINISHED", "Finish Work", "FW"),
	END_LEAD("ENDED", "End Lead", "EL"),
	LOAD_VENDOR_FILE("VENDOR FILE LOADED", "Load Vendor File", "LVF");
	
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
