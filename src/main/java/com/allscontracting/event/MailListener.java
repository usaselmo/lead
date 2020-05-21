package com.allscontracting.event;

import org.springframework.stereotype.Component;

import com.allscontracting.model.Lead;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class MailListener implements DomainEventListener<Lead> {

	@Override
	public void update(Event event, Lead lead) {
		log.info("MailListener called. event: {}, lead: {}", event, lead);
	}

}
