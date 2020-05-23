package com.allscontracting.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.EventTypeDispatcher;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.repo.jpaimpl.EventoLogJpaRepository;
import com.allscontracting.repo.jpaimpl.LeadJpaRepository;

@Service
public class LeadService {

	private static final int LEADS_PER_PAGE = 5;
	@Autowired	private LeadJpaRepository leadRepo;
	@Autowired private EventTypeDispatcher eventDispatcher;
	@Autowired private EventManager eventManager;
	@Autowired private EventoLogJpaRepository eventLogRepo;

	public List<Lead> listLeads(int pageRange) throws Exception {
		if(pageRange<0)
			pageRange=0;
		return leadRepo.findAll(new PageRequest(pageRange, LEADS_PER_PAGE, new Sort(Sort.Direction.ASC, "date") )).getContent();
	}

	public void drop() throws Exception {
		leadRepo.deleteAll();
	}

	public long getLeadsTotal() throws Exception {
		return this.leadRepo.count();
	}
	
	public List<EventType> findNextEvents(String leadId) {
		Lead lead = this.leadRepo.findOne(leadId);
		EventType currentEvent = lead.getEvent();
		if(null == currentEvent)
			currentEvent = EventType.BEGIN;
		return this.eventDispatcher.findNextEvents(currentEvent);
	}

	@Transactional
	public void scheduleAVisit(String leadId, Date visit) {
		Lead lead = this.leadRepo.findOne(leadId);
		lead.setVisit(visit);
		lead.setEvent(EventType.SCHEDULE_VISIT);
		this.leadRepo.save(lead);
		this.eventManager.notifyAllListeners(new VisitScheduledEvent(lead, lead.getClient(), new Date()));
	}

	public List<EventLog> findEventLogs(String leadId) {
		return this.eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId);
	}

	@Transactional
	public void fireEvent(String event, String leadId) {
		Lead lead = this.leadRepo.findOne(leadId);
		lead.setEvent(EventType.valueOf(event));
		this.leadRepo.save(lead);
		this.eventLogRepo.save(EventLog.builder().eventTime(new Date()).eventType(EventType.valueOf(event)).objectId(leadId).objectName(Lead.class.getSimpleName()).userId(0L).build());
	}

}
