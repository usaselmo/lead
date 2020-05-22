package com.allscontracting.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.service.MailService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Component
public class EmailListener implements DomainListener<EstimateScheduledEvent> {

	@Autowired MailService mailService;

	@Override
	public void doAction(EstimateScheduledEvent domainEvent) {
		System.out.println("Email listener....");
	}

}
