package com.allscontracting.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.event.DomainEvent;
import com.allscontracting.event.EventType;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.service.MailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailListener implements DomainListener {

	@Autowired
	MailService mailService;

	@Override
	public void update(DomainEvent domainEvent) {
		log.info("EmailListener fired....");
		if(domainEvent.getEventType().equals(EventType.SCHEDULE_VISIT.toString()))
			this.handleEstimateScheduled((VisitScheduledEvent) domainEvent);
	}

	private void handleEstimateScheduled(VisitScheduledEvent domainEvent) {
		// TODO Auto-generated method stub
	}

}
