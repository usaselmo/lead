package com.allscontracting.event;

import java.util.stream.Stream;

public enum Event { 
	
	BEGIN("CONTACTING", "START OVER", "BE", true),
	ASSIGN_TO_ESTIMATOR("ASSIGNED TO ESTIMATOR", "ASSIGN TO ESTIMATOR", "AE", true),
	BID("BIDDING", "BID", "BD", true),
	CREATE_PROPOSAL("PROPOSAL CREATED", "CREATE PROPOSAL", "CP", true),
	SEND_PROPOSAL("PROPOSAL SENT", "SEND PROPOSAL", "SP", true),
	ACCEPT_PROPOSAL("PROPOSAL ACCEPTED", "ACCEPT PROPOSAL", "AP", true),
	BEGIN_WORK("WORK IN PROGRESS", "BEGIN WORK", "BW", true),
	FINISH_WORK("WORK FINISHED", "FINISH WORK", "FW", true),
	END_LEAD("ENDED", "END LEAD", "EL", true),
	
	
	LOAD_VENDOR_FILE("VENDOR FILE LOADED", "LOAD VENDOR FILE", "LVF", false),
	CREATE("CREATED", "CREATE", "C", false),
	UPDATE("UPDATED", "UPDATE", "U", false),
	EMAIL_SENT("EMAIL SENT", "SEND EMAIL", "SE", false),
	;
	
	private String status;
	private String action;
	private String abbreviation;
	private boolean showInMenu;
	
	Event(String status, String action, String abbreviation, boolean showInMenu) {
		this.status = status;
		this.action = action;
		this.abbreviation = abbreviation;
		this.showInMenu = showInMenu;
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

	public boolean isShowInMenu() {
		return showInMenu;
	}

	public static Event reverse(String description) {
		return Stream.of(Event.values()).filter(type->type.toString().equalsIgnoreCase(description)).findFirst().get();
	}
	
}
