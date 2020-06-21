package com.allscontracting.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.EventTypeDispatcher;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ClientRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.tradutor.impl.NetworxLeadTranslaterImpl;

@Service
public class LeadService {

	@Autowired private LeadRepository leadRepo;
	@Autowired private EventTypeDispatcher eventDispatcher;
	@Autowired private EventManager eventManager;
	@Autowired private EventoLogRepository eventLogRepo;
	@Autowired private ClientRepository clientRepo;
	
	public List<Lead> listLeads(int pageRange, int lines, EventType eventType) throws Exception {
		if(pageRange<0)
			pageRange=0;
		PageRequest pageable = new PageRequest(pageRange, lines, new Sort(Sort.Direction.DESC, "date") );
		if(eventType==null)
			return leadRepo.findAll(pageable).getContent();
		else
			return leadRepo.findAllByEvent(pageable, eventType).getContent();
	}

	public void drop(Long userId) throws Exception {
		leadRepo.deleteAll();
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), null, "All leads deleted", userId));
	}

	public long getLeadsTotal(EventType eventType) throws Exception {
		if (StringUtils.isEmpty(eventType)) 
			return this.leadRepo.count();
		else
			return this.leadRepo.countByEvent(eventType);
	}

	public List<EventType> findNextEvents(String leadId) {
		Lead lead = this.leadRepo.findOne(leadId);
		EventType currentEvent = lead.getEvent();
		if(null == currentEvent)
			currentEvent = EventType.BEGIN;
		return this.eventDispatcher.findNextEvents(currentEvent);
	}

	@Transactional
	public void scheduleAVisit(String leadId, Date visitDateTime, Long userId) {
		Lead lead = this.leadRepo.findOne(leadId);
		lead.setVisit(visitDateTime);
		lead.setEvent(EventType.SCHEDULE_VISIT);
		this.leadRepo.save(lead);
		this.eventManager.notifyAllListeners(new VisitScheduledEvent(lead, lead.getClient(), visitDateTime, userId));
	}

	@Transactional
	public void fireEventToLead(String event, String leadId, Long userId) {
		Lead lead = this.leadRepo.findOne(leadId);
		EventType eventType = EventType.reverse(event);
		lead.setEvent(eventType); 
		this.leadRepo.save(lead); 
		this.eventLogRepo.save(new EventLog(Lead.class.getSimpleName(), lead.getId(), eventType.toString(), new Date(), userId, ""));
//		this.eventLogRepo.save(EventLog.builder().eventTime(new Date()).eventType(eventType.toString()).objectId(leadId).objectName(Lead.class.getSimpleName()).userId(0L).build());
	}

	public List<EventLog> findLeadEventLogs(String leadId) {
		return this.eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId);
	}

	public List<Proposal> findProposals(String leadId) {
		return this.leadRepo.findProposals(leadId);
	}

	public Lead addNewNote(String leadId, String note) {
		Lead lead = this.leadRepo.findOne(leadId);
		lead.addNote(note);
		lead = this.leadRepo.save(lead);
		return lead;
	}

	public List<Lead> search(String text) {
		//limited to 100 results
		return this.leadRepo.search(text, new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "date")));
	}

	@Transactional
	public Lead saveNewLead(Lead lead, Long userId) {
		if(lead.getClient().getId() != null && this.clientRepo.exists(lead.getClient().getId())) {
			lead.setClient(this.clientRepo.findOne(lead.getClient().getId()));
		}else {
			lead.setClient(this.clientRepo.save(lead.getClient()));
		}
		lead.setId(NetworxLeadTranslaterImpl.defineId(lead.getClient().getEmail(), lead.getClient().getPhone()));
		lead.setDate(new Date());
		lead.setEvent(EventType.BEGIN); 
		lead.setFee(BigDecimal.ZERO);
		lead = this.leadRepo.save(lead);
		this.fireEventToLead(EventType.BEGIN.toString(), lead.getId(), userId);
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), lead.getId(), "New Lead created: " + lead.getId(), userId));
		return lead;
	}

	public List<String> getLeadTypes() {
		return this.leadRepo.findByType(); 
	}

}
