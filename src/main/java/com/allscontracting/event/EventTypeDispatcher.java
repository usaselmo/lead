package com.allscontracting.event;

import static com.allscontracting.event.EventType.BEGIN;
import static com.allscontracting.event.EventType.BEGIN_WORK;
import static com.allscontracting.event.EventType.END_LEAD;
import static com.allscontracting.event.EventType.ESTIMATE_SCHEDULED;
import static com.allscontracting.event.EventType.FINISH_WORK;
import static com.allscontracting.event.EventType.MARK_AS_VISITED;
import static com.allscontracting.event.EventType.RECEIVE_PAYMENT;
import static com.allscontracting.event.EventType.SEND_ESTIMATE;
import static com.allscontracting.event.EventType.SEND_INVOICE;
import static com.allscontracting.event.EventType.WASTE_LEAD;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EventTypeDispatcher {

	public List<EventType> findNextEvents(EventType event) {

		EventType[] START = { ESTIMATE_SCHEDULED, WASTE_LEAD, END_LEAD };
		EventType[] ESTIMATE_SCHEDULED = { MARK_AS_VISITED, WASTE_LEAD, END_LEAD };
		EventType[] MARK_AS_VISITED = { SEND_ESTIMATE, WASTE_LEAD, END_LEAD };
		EventType[] SEND_ESTIMATE = { BEGIN_WORK, WASTE_LEAD, END_LEAD };
		EventType[] BEGIN_WORK = { FINISH_WORK, WASTE_LEAD, END_LEAD };
		EventType[] FINISH_WORK = { SEND_INVOICE, END_LEAD };
		EventType[] SEND_INVOICE = { RECEIVE_PAYMENT, END_LEAD };
		EventType[] RECEIVE_PAYMENT = { END_LEAD };
		EventType[] WASTE_LEAD = { END_LEAD };
		EventType[] END_LEAD = {BEGIN};

		switch (event) {
		case BEGIN:
			return Arrays.asList(START);
		case ESTIMATE_SCHEDULED:
			return Arrays.asList(ESTIMATE_SCHEDULED);
		case MARK_AS_VISITED:
			return Arrays.asList(MARK_AS_VISITED);
		case SEND_ESTIMATE:
			return Arrays.asList(SEND_ESTIMATE);
		case BEGIN_WORK:
			return Arrays.asList(BEGIN_WORK);
		case FINISH_WORK:
			return Arrays.asList(FINISH_WORK);
		case SEND_INVOICE:
			return Arrays.asList(SEND_INVOICE);
		case RECEIVE_PAYMENT:
			return Arrays.asList(RECEIVE_PAYMENT);
		case WASTE_LEAD:
			return Arrays.asList(WASTE_LEAD);
		case END_LEAD:
			return Arrays.asList(END_LEAD);
		default:
			return Collections.emptyList();
		}

	}

}
