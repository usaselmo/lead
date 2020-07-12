package com.allscontracting.event;

import static com.allscontracting.event.EventType.ASSIGN_TO_ESTIMATOR;
import static com.allscontracting.event.EventType.BEGIN;
import static com.allscontracting.event.EventType.BEGIN_WORK;
import static com.allscontracting.event.EventType.CREATE_PROPOSAL;
import static com.allscontracting.event.EventType.END_LEAD;
import static com.allscontracting.event.EventType.FINISH_WORK;
import static com.allscontracting.event.EventType.SEND_PROPOSAL;
import static com.allscontracting.event.EventType.ACCEPT_PROPOSAL;
import static com.allscontracting.event.EventType.BID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventTypeDispatcher {

	public List<EventType> findNextEvents(EventType event) {
/* 	
  -> BEGIN("Contacting", "Start Over", "BE"),
	-> ASSIGN_TO_ESTIMATOR("Assigned to Estimator", "Assign to Estimator", "AE"),
	-> SCHEDULE_VISIT("Visiting", "Schedule a Visit", "SV"),
	-> MARK_AS_VISITED("Visited", "Mark as Visited", "MV"),
	-> CREATE_PROPOSAL("Proposal Created", "Create Proposal", "CP"),
	-> SEND_PROPOSAL("PROPOSAL SENT", "Send Proposal", "SP"),
	ACCEPT_PROPOSAL("PROPOSAL ACCEPTED", "Accept Proposal", "AP"),
	BEGIN_WORK("WORK IN PROGRESS", "Begin Work", "BW"),
	FINISH_WORK("Work Finished", "Finish Work", "FW"),
	END_LEAD("Ended", "End Lead", "EL"),
	LOAD_VENDOR_FILE("Vendor File Loaded", "Load Vendor File", "LVF");
 */
		EventType[] VENDOR_FILE_LOADED = { BEGIN };
		EventType[] BEGIN_ = {ASSIGN_TO_ESTIMATOR, BID, CREATE_PROPOSAL, END_LEAD };
		EventType[] ASSIGN_TO_ESTIMATOR = {BID, CREATE_PROPOSAL, END_LEAD };
		EventType[] BID = { BEGIN_WORK, END_LEAD };
		EventType[] CREATE_ESTIMATE = { SEND_PROPOSAL, END_LEAD };
		EventType[] SEND_ESTIMATE = { ACCEPT_PROPOSAL, END_LEAD };
		EventType[] ACCEPT_PROPOSAL = { BEGIN_WORK, END_LEAD };
		EventType[] BEGIN_WORK = { FINISH_WORK,  END_LEAD };
		EventType[] FINISH_WORK = {};
		EventType[] END_LEAD = {BEGIN};

		switch (event) {
		case LOAD_VENDOR_FILE:
			return Arrays.asList(VENDOR_FILE_LOADED);
		case BEGIN:
			return Arrays.asList(BEGIN_);
		case ASSIGN_TO_ESTIMATOR:
			return Arrays.asList(ASSIGN_TO_ESTIMATOR);
		case BID:
			return Arrays.asList(BID);
		case CREATE_PROPOSAL:
			return Arrays.asList(CREATE_ESTIMATE);
		case SEND_PROPOSAL:
			return Arrays.asList(SEND_ESTIMATE);
		case ACCEPT_PROPOSAL:
			return Arrays.asList(ACCEPT_PROPOSAL);
		case BEGIN_WORK:
			return Arrays.asList(BEGIN_WORK);
		case FINISH_WORK:
			return Arrays.asList(FINISH_WORK);
		case END_LEAD:
			return Arrays.asList(END_LEAD);
		default:
			return Collections.emptyList();
		}

	}

}
