package com.allscontracting.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.LeadDTO;
import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.EventTypeDispatcher;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.ClientRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.UserRepository;

@Service
public class LeadService {

	@Autowired private LeadRepository leadRepo;
	@Autowired private EventTypeDispatcher eventDispatcher;
	@Autowired private EventManager eventManager;
	@Autowired private EventoLogRepository eventLogRepo;
	@Autowired private ClientRepository clientRepo;
	@Autowired private UserRepository userRepo;
	
	public List<LeadDTO> listLeads(int pageRange, int lines, EventType eventType) throws Exception {
		if(pageRange<0)
			pageRange=0;
		PageRequest pageable = PageRequest.of(pageRange, lines, new Sort(Sort.Direction.DESC, "date") );
		if(eventType==null)
			return leadRepo.findAll(pageable).getContent().stream().map(l->LeadDTO.leadToDTO(l)).collect(Collectors.toList());
		else
			return leadRepo.findAllByEvent(pageable, eventType).getContent().stream().map(l->LeadDTO.leadToDTO(l)).collect(Collectors.toList());
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

	public List<EventType> findNextEvents(String leadId) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		EventType currentEvent = lead.getEvent();
		if(null == currentEvent)
			currentEvent = EventType.BEGIN;
		return this.eventDispatcher.findNextEvents(currentEvent);
	}

	@Transactional
	public void scheduleAVisit(String leadId, Date visitDateTime, Long userId) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		lead.setVisit(visitDateTime);
		lead.setEvent(EventType.SCHEDULE_VISIT);
		this.leadRepo.save(lead);
		this.eventManager.notifyAllListeners(new VisitScheduledEvent(lead, lead.getClient(), visitDateTime, userId));
	}

	@Transactional
	public void fireEventToLead(String event, String leadId, Long userId) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		EventType eventType = EventType.reverse(event);
		lead.setEvent(eventType); 
		this.leadRepo.save(lead); 
		this.eventLogRepo.save(new EventLog(Lead.class.getSimpleName(), String.valueOf(lead.getId()), eventType.toString(), new Date(), userId, ""));
//		this.eventLogRepo.save(EventLog.builder().eventTime(new Date()).eventType(eventType.toString()).objectId(leadId).objectName(Lead.class.getSimpleName()).userId(0L).build());
	}

	public List<EventLog> findLeadEventLogs(String leadId) {
		return this.eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId);
	}

	public List<Proposal> findProposals(String leadId) {
		return this.leadRepo.findProposals(Long.valueOf(leadId));
	}

	public Lead addNewNote(String leadId, String note) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		lead.addNote(note);
		lead = this.leadRepo.save(lead);
		return lead;
	}

	public List<LeadDTO> search(String text) {
		//limited to 100 results
		return this.leadRepo.search(text, new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "date"))).stream().map(l->LeadDTO.leadToDTO(l)).collect(Collectors.toList());
	}

	@Transactional
	public Lead saveNewLead(Lead lead, Long userId) throws LeadsException {
		if(lead.getClient().getId() != null && this.clientRepo.existsById(lead.getClient().getId())) {
			lead.setClient(this.clientRepo.findById(lead.getClient().getId()).orElseThrow(()->new LeadsException("Client not found")));
		}else {
			lead.setClient(this.clientRepo.save(lead.getClient()));
		}
		//lead.setId(NetworxLeadTranslaterImpl.defineId(lead.getClient().getEmail(), lead.getClient().getPhone()));
		lead.setDate(new Date());
		lead.setEvent(EventType.BEGIN); 
		lead.setFee(BigDecimal.ZERO);
		lead = this.leadRepo.save(lead);
		this.fireEventToLead(EventType.BEGIN.toString(), String.valueOf(lead.getId()), userId);
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), String.valueOf(lead.getId()), "New Lead created: " + lead.getId(), userId));
		return lead;
	}

	public List<String> getLeadTypes() {
		return this.leadRepo.findByType(); 
	}

	public LeadDTO assignEstimator(String leadId, String estimatorId) throws LeadsException {
		Lead lead = leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		User estimator = userRepo.findById(Long.valueOf(estimatorId)).orElseThrow(() -> new LeadsException("Estimator not found"));
		lead.setEstimator(estimator);
		return LeadDTO.leadToDTO(leadRepo.save(lead));
	}

}
