package com.allscontracting.event;

import static com.allscontracting.event.Event.ASSIGN_TO_ESTIMATOR;
import static com.allscontracting.event.Event.BEGIN;
import static com.allscontracting.event.Event.BEGIN_WORK;
import static com.allscontracting.event.Event.CREATE_PROPOSAL;
import static com.allscontracting.event.Event.END_LEAD;
import static com.allscontracting.event.Event.FINISH_WORK;
import static com.allscontracting.event.Event.SEND_PROPOSAL;
import static com.allscontracting.event.Event.ACCEPT_PROPOSAL;
import static com.allscontracting.event.Event.BID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

	public List<Event> findNextEvents(Event event) {
		
		Event[] VENDOR_FILE_LOADED = { BEGIN };
		Event[] BEGIN_ = {ASSIGN_TO_ESTIMATOR, BID, CREATE_PROPOSAL, END_LEAD };
		Event[] ASSIGN_TO_ESTIMATOR = {BID, CREATE_PROPOSAL, END_LEAD };
		Event[] BID = { BEGIN_WORK, END_LEAD };
		Event[] CREATE_ESTIMATE = { SEND_PROPOSAL, END_LEAD };
		Event[] SEND_ESTIMATE = { ACCEPT_PROPOSAL, END_LEAD };
		Event[] ACCEPT_PROPOSAL = { BEGIN_WORK, END_LEAD };
		Event[] BEGIN_WORK = { FINISH_WORK,  END_LEAD };
		Event[] FINISH_WORK = {Event.PAY};
		Event[] END_LEAD = {BEGIN};

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
