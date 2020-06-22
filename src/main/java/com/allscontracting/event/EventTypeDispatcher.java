package com.allscontracting.event;

import static com.allscontracting.event.EventType.BEGIN;
import static com.allscontracting.event.EventType.BEGIN_WORK;
import static com.allscontracting.event.EventType.CREATE_PROPOSAL;
import static com.allscontracting.event.EventType.END_LEAD;
import static com.allscontracting.event.EventType.FINISH_WORK;
import static com.allscontracting.event.EventType.MARK_AS_VISITED;
import static com.allscontracting.event.EventType.RECEIVE_PAYMENT;
import static com.allscontracting.event.EventType.SCHEDULE_VISIT;
import static com.allscontracting.event.EventType.SEND_PROPOSAL;
import static com.allscontracting.event.EventType.SEND_INVOICE;
import static com.allscontracting.event.EventType.ASSIGN_TO_ESTIMATOR;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventTypeDispatcher {

	public List<EventType> findNextEvents(EventType event) {

		EventType[] VENDOR_FILE_LOADED = { BEGIN };
		EventType[] BEGIN_ = {EventType.CONTACT_QUALIFY, ASSIGN_TO_ESTIMATOR, SCHEDULE_VISIT, MARK_AS_VISITED, CREATE_PROPOSAL, END_LEAD };
		EventType[] CONTACT_QUALIFY = { ASSIGN_TO_ESTIMATOR, SCHEDULE_VISIT, MARK_AS_VISITED, CREATE_PROPOSAL, END_LEAD };
		EventType[] ASSIGN_TO_ESTIMATOR = {SCHEDULE_VISIT, MARK_AS_VISITED, CREATE_PROPOSAL, END_LEAD };
		EventType[] ESTIMATE_SCHEDULED = { MARK_AS_VISITED, CREATE_PROPOSAL, END_LEAD };
		EventType[] MARK_AS_VISITED = { CREATE_PROPOSAL, END_LEAD };
		EventType[] CREATE_ESTIMATE = { SEND_PROPOSAL, END_LEAD };
		EventType[] SEND_ESTIMATE = { BEGIN_WORK, END_LEAD };
		EventType[] BEGIN_WORK = { FINISH_WORK,  END_LEAD };
		EventType[] FINISH_WORK = { SEND_INVOICE, END_LEAD };
		EventType[] SEND_INVOICE = { RECEIVE_PAYMENT, END_LEAD };
		EventType[] RECEIVE_PAYMENT = { END_LEAD };
		EventType[] END_LEAD = {BEGIN};

		switch (event) {
		case LOAD_VENDOR_FILE:
			return Arrays.asList(VENDOR_FILE_LOADED);
		case BEGIN:
			return Arrays.asList(BEGIN_);
		case CONTACT_QUALIFY:
			return Arrays.asList(CONTACT_QUALIFY);
		case ASSIGN_TO_ESTIMATOR:
			return Arrays.asList(ASSIGN_TO_ESTIMATOR);
		case SCHEDULE_VISIT:
			return Arrays.asList(ESTIMATE_SCHEDULED);
		case MARK_AS_VISITED:
			return Arrays.asList(MARK_AS_VISITED);
		case CREATE_PROPOSAL:
			return Arrays.asList(CREATE_ESTIMATE);
		case SEND_PROPOSAL:
			return Arrays.asList(SEND_ESTIMATE);
		case BEGIN_WORK:
			return Arrays.asList(BEGIN_WORK);
		case FINISH_WORK:
			return Arrays.asList(FINISH_WORK);
		case SEND_INVOICE:
			return Arrays.asList(SEND_INVOICE);
		case RECEIVE_PAYMENT:
			return Arrays.asList(RECEIVE_PAYMENT);
		case END_LEAD:
			return Arrays.asList(END_LEAD);
		default:
			return Collections.emptyList();
		}

	}

}
