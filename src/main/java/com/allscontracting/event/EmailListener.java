package com.allscontracting.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.service.MailService;

@Component
public class EmailListener implements DomainListener {

	@Autowired
	MailService mailService;

	@Override
	public void update(DomainEvent domainEvent) {

		switch (domainEvent.getEventType()) {
		case SCHEDULE_VISIT:
			this.handleEstimateScheduled((VisitScheduledEvent) domainEvent);
			break;
		default:
			break;
		}

		System.out.println("Email listener agindo....., Autowired Mail Service: " + this.mailService);
		System.out.println("Tipo de evento: " + domainEvent.getEventType());
		System.out.println(((VisitScheduledEvent) domainEvent).getVisitSchedule());
	}

	private void handleEstimateScheduled(VisitScheduledEvent domainEvent) {
		// TODO Auto-generated method stub
	}

}
