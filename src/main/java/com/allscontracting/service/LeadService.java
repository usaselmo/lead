package com.allscontracting.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.EventLogDTO;
import com.allscontracting.dto.EventTypeDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.EventTypeDispatcher;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.ClientRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadService {

	private final LeadRepository leadRepo;
	private final EventTypeDispatcher eventDispatcher; 
	private final EventManager eventManager;
	private final EventoLogRepository eventLogRepo;
	private final ClientRepository clientRepo;
	private final UserRepository userRepo;
	
	public List<LeadDTO> listLeads(int pageRange, int lines, EventType eventType) throws Exception {
		if(pageRange<0)
			pageRange=0;
		PageRequest pageable = PageRequest.of(pageRange, lines, new Sort(Sort.Direction.DESC, "date") );
		if(eventType==null)
			return leadRepo.findAll(pageable).getContent().stream().map(l->LeadDTO.of(l)).collect(Collectors.toList());
		else
			return leadRepo.findAllByEvent(pageable, eventType).getContent().stream().map(l->LeadDTO.of(l)).collect(Collectors.toList());
	}

	public List<EventTypeDTO> findNextEvents(String leadId) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		EventType currentEvent = lead.getEvent();
		if(null == currentEvent)
			currentEvent = EventType.BEGIN;
		return eventDispatcher.findNextEvents(currentEvent).stream().map(et->EventTypeDTO.of(et)).collect(Collectors.toList());
	}

	@Transactional
	public LeadDTO scheduleAVisit(String leadId, String time, User user) throws LeadsException, ParseException {
		Date visitDateTime = Converter.stringToDate(time, Converter.MM_dd_yy_hh_mm);
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		lead.setVisit(visitDateTime);
		return LeadDTO.of(leadRepo.save(lead));
	}

	@Transactional
	public void fireEventToLead(String event, String leadId, User user) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		EventType eventType = EventType.reverse(event);
		lead.setEvent(eventType); 
		this.leadRepo.save(lead); 
		this.eventLogRepo.save(new EventLog(Lead.class.getSimpleName(), String.valueOf(lead.getId()), eventType.toString(), new Date(), user, ""));
//		this.eventLogRepo.save(EventLog.builder().eventTime(new Date()).eventType(eventType.toString()).objectId(leadId).objectName(Lead.class.getSimpleName()).userId(0L).build());
	}

	public List<EventLogDTO> findLeadEventLogs(String leadId) {
		return eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId).stream().map(e->EventLogDTO.of(e)).collect(Collectors.toList());
	}

	public List<Proposal> findProposals(String leadId) {
		return this.leadRepo.findProposals(Long.valueOf(leadId));
	}

	public LeadDTO addNewNote(String leadId, String note) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(()->new LeadsException("Lead not found"));
		lead.addNote(note);
		lead = this.leadRepo.save(lead);
		return LeadDTO.of(lead);
	}

	public List<LeadDTO> search(String text) {
		//limited to 100 results
		return this.leadRepo.search(text, PageRequest.of(0, 100, new Sort(Sort.Direction.DESC, "date"))).stream().map(l->LeadDTO.of(l)).collect(Collectors.toList());
	}

	@Transactional
	public LeadDTO saveNewLead(LeadDTO leadDTO, User user) throws LeadsException{
		Lead lead = LeadDTO.toLead(leadDTO);
		if(lead.getClient().getId() != null && this.clientRepo.existsById(lead.getClient().getId())) {
			lead.setClient(this.clientRepo.findById(lead.getClient().getId()).orElseThrow(()->new LeadsException("Client not found")));
		}else {
			lead.setClient(this.clientRepo.save(lead.getClient()));
		}
		lead.setDate(new Date());
		lead.setEvent(EventType.BEGIN); 
		lead.setFee(BigDecimal.ZERO);
		lead = this.leadRepo.save(lead);
		this.fireEventToLead(EventType.BEGIN.toString(), String.valueOf(lead.getId()), user);
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), String.valueOf(lead.getId()), "New Lead created: " + lead.getId(), user));
		return LeadDTO.of(lead);
	}

	public List<String> getLeadTypes() {
		return this.leadRepo.findByType(); 
	}

	public LeadDTO assignEstimator(String leadId, String estimatorId, User user) throws LeadsException {
		Lead lead = leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		User estimator = userRepo.findById(Long.valueOf(estimatorId)).orElseThrow(() -> new LeadsException("Estimator not found"));
		lead.setEstimator(estimator);
		lead.setEvent(EventType.ASSIGN_TO_ESTIMATOR);
		LeadDTO leadDTO = LeadDTO.of(leadRepo.save(lead));
		this.fireEventToLead(EventType.ASSIGN_TO_ESTIMATOR.toString(), String.valueOf(lead.getId()), user);
		return leadDTO;
	}

	public long getLeadsTotal(EventType eventType) throws Exception {
		if (StringUtils.isEmpty(eventType)) 
			return this.leadRepo.count();
		else
			return this.leadRepo.countByEvent(eventType);
	}

}
