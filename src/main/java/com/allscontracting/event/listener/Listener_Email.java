package com.allscontracting.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.event.DomainEvent;
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
	}

}
