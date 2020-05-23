package com.allscontracting.event;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import com.allscontracting.model.EventLog;
import com.allscontracting.repo.jpaimpl.EventoLogJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventLogListener implements DomainListener {
	
	@Autowired EventoLogJpaRepository eventLogRepo;

	@Override
	@Transactional
	public void update(DomainEvent de) {
		log.info("EventLogListener fired....{}", de);
		EventLog event = new EventLog(de.getObjectName(), de.getObjectId(), de.getEventType(), de.getEventTime(), Long.valueOf("0"));
		this.eventLogRepo.save(event);
	}

}
