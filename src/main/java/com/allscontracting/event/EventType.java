package com.allscontracting.event;

public enum EventType {
	BEGIN("Started"), 
	CONTACT_QUALIFY("Contact and Qualify"), 
	SCHEDULE_VISIT("Visit scheduled"), 
	MARK_AS_VISITED("Marked as visited"),
	CREATE_PROPOSAL("Proposal created"),
	SEND_PROPOSAL("Proposal sent"), 
	BEGIN_WORK("Work started"), 
	FINISH_WORK("Work finished"), 
	SEND_INVOICE("Invoide sent"),
	RECEIVE_PAYMENT("Payment Received") , 
	END_LEAD("Done"), 
	LOAD_VENDOR_FILE("Vendor file loaded");
	
	private String description;
	
	EventType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
}
