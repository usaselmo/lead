package com.allscontracting.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.EventDTO;
import com.allscontracting.dto.EventLogDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.event.Event;
import com.allscontracting.event.EventDispatcher;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.PersonRepository;
import com.allscontracting.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadService {

	private final LeadRepository leadRepo;
	private final EventDispatcher eventDispatcher;
	private final EventoLogRepository eventLogRepo;
	private final PersonRepository personRepo;
	private final UserRepository userRepo;
	private final LogService logg;
	private final CompanyRepository companyRepo;

	public List<LeadDTO> listLeads(int pageRange, int lines, String text, Event event) throws LeadsException {
		if (pageRange < 0)
			pageRange = 0;
		List<Event> events = (event == null) ? Arrays.asList(Event.values()) : Arrays.asList(event);
		PageRequest pageable = PageRequest.of(pageRange, lines, new Sort(Sort.Direction.DESC, "date"));
		List<LeadDTO> res = search(text, events, pageable);
		return res;
	}

	private List<LeadDTO> search(String text, List<Event> events, PageRequest pageable) {
		List<Lead> leads = leadRepo.search(text, events, pageable);
		leads.addAll(leadRepo.search2(text, events, pageable));
		leads.addAll(leadRepo.search3(text, events, pageable));
		leads.addAll(leadRepo.search4(text, events, pageable));
		return leads.stream().map(l -> LeadDTO.of(l)).collect(Collectors.toList());
	}

	public List<EventDTO> findNextEvents(String leadId) {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElse(null);
		Event currentEvent = lead.getEvent();
		if (null == currentEvent)
			currentEvent = Event.BEGIN;
		return eventDispatcher.findNextEvents(currentEvent).stream().map(et -> EventDTO.of(et)).collect(Collectors.toList());
	}

	@Transactional
	public LeadDTO scheduleAVisit(String leadId, String time, User user) throws LeadsException, ParseException {
		Date visitDateTime = Converter.stringToDate(time, Converter.MM_dd_yy_hh_mm);
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		lead.setVisit(visitDateTime);
		LeadDTO leadDTO = LeadDTO.of(leadRepo.save(lead));
		completeLead(leadDTO);
		return leadDTO;
	}

	public List<EventLogDTO> findLeadEventLogs(String leadId) {
		return eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId).stream().map(e -> EventLogDTO.of(e)).collect(Collectors.toList());
	}

	public List<Proposal> findProposals(String leadId) {
		return this.leadRepo.findProposals(Long.valueOf(leadId));
	}

	public LeadDTO addNewNote(String leadId, String note) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		lead.addNote(note);
		lead = this.leadRepo.save(lead);
		LeadDTO leadDTO = LeadDTO.of(lead);
		completeLead(leadDTO);
		return leadDTO;
	}

//	public List<LeadDTO> search(String text) {
//		return this.leadRepo.search(text, null, PageRequest.of(0, 100, new Sort(Sort.Direction.DESC, "date"))).stream().map(l -> LeadDTO.of(l)).collect(Collectors.toList());
//	}

	@Transactional
	public LeadDTO save(LeadDTO leadDTO, User user) throws LeadsException {
		Lead lead = new Lead();

		// deprecated properties
		lead.setFee(BigDecimal.ZERO);
		lead.setType("Concrete");
		lead.setVendor(Lead.Vendor.EMAIL);

		// actual properties
		lead.setEvent(Event.BEGIN);
		lead.setDate(new Date());
		lead.setTitle(leadDTO.getTitle());
		lead.setCompany(leadDTO.getCompany() == null ? null : this.companyRepo.findById(leadDTO.getCompany().getId()).orElse(null));
		lead.setContact(leadDTO.getContact() == null ? null : this.personRepo.findById(Long.valueOf(leadDTO.getContact().getId())).orElse(null));
		lead.setClient(leadDTO.getClient() == null ? null : this.personRepo.findById(Long.valueOf(leadDTO.getClient().getId())).orElse(null));
		lead.setEstimator(leadDTO.getEstimator() == null ? null : this.userRepo.findById(Long.valueOf(leadDTO.getEstimator().getId())).orElse(null));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit()));
		lead.setDueDate(Converter.convertToDate(leadDTO.getDueDate()));
		lead.setDescription(leadDTO.getDescription());
		lead.setAddress(leadDTO.getAddress());

		lead = this.leadRepo.save(lead);
		logg.newLeadCreated(String.valueOf(lead.getId()), lead.getClient(), user);
		leadDTO = LeadDTO.of(lead);
		completeLead(leadDTO);
		return leadDTO;
	}

	public LeadDTO update(LeadDTO leadDTO) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadDTO.getId())).orElseThrow(() -> new LeadsException("Lead not found"));
		lead.setTitle(leadDTO.getTitle());
		lead.setCompany(leadDTO.getCompany() == null || leadDTO.getCompany().getId() == null ? null : this.companyRepo.findById(leadDTO.getCompany().getId()).orElse(null));
		lead.setContact(leadDTO.getContact() == null || StringUtils.isEmpty(leadDTO.getContact().getId()) ? null : this.personRepo.findById(Long.valueOf(leadDTO.getContact().getId())).orElse(null));
		lead.setClient(leadDTO.getClient() == null || StringUtils.isEmpty(leadDTO.getClient().getId()) ? null : this.personRepo.findById(Long.valueOf(leadDTO.getClient().getId())).orElse(null));
		lead.setEstimator(leadDTO.getEstimator() == null || StringUtils.isEmpty(leadDTO.getEstimator().getId()) ? null : this.userRepo.findById(Long.valueOf(leadDTO.getEstimator().getId())).orElse(null));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit(), Converter.MM_dd_yyyy_hh_mm));
		lead.setDueDate(Converter.convertToDate(leadDTO.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
		lead.setAddress(leadDTO.getAddress());
		lead.setDescription(leadDTO.getDescription());
		leadDTO = LeadDTO.of(this.leadRepo.save(lead));
		completeLead(leadDTO);
		return leadDTO;
	}

	public List<String> getLeadTypes() {
		return this.leadRepo.findByType();
	}

	public LeadDTO assignEstimator(String leadId, String estimatorId, User user) throws LeadsException {
		Lead lead = leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		User estimator = userRepo.findById(Long.valueOf(estimatorId)).orElseThrow(() -> new LeadsException("Estimator not found"));
		lead.setEstimator(estimator);
		lead.setEvent(Event.ASSIGN_TO_ESTIMATOR);
		LeadDTO leadDTO = LeadDTO.of(leadRepo.save(lead));
		logg.event(Lead.class, String.valueOf(lead.getId()), Event.ASSIGN_TO_ESTIMATOR, user);
		leadRepo.save(lead);
		completeLead(leadDTO);
		return leadDTO;
	}

	public long getLeadsTotal(Event event) throws LeadsException {
		if (StringUtils.isEmpty(event))
			return this.leadRepo.count();
		else {
			List<Event> events = (event == null) ? Arrays.asList(Event.values()) : Arrays.asList(event);
			return this.leadRepo.countByEvent(events);
		}
	}

	@Transactional
	public LeadDTO fireEvent(String id, Event event, User user) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(id)).orElseThrow(() -> new LeadsException("Could not find Lead"));
		lead.setEvent(event);
		lead = leadRepo.save(lead);
		logg.event(Lead.class, id, event, user);
		LeadDTO leadDTO = LeadDTO.of(lead);
		completeLead(leadDTO);
		return leadDTO;
	}

	public LeadEntity list(int pageRange, int lines, Event event, String text) throws LeadsException {
		List<LeadDTO> leads = listLeads(pageRange, lines, text, event);
		long leadsTotalPrice = leads.stream().mapToLong(l -> l.getPrice()).sum();
		LeadEntity res = LeadEntity.builder().leads(leads).leadsTotalPrice(leadsTotalPrice).leadTypes(getLeadTypes())
				.events(Stream.of(Event.values()).filter(e -> e.isShowInMenu() == true).map(et -> EventDTO.of(et)).collect(Collectors.toList())).totalLeads(getLeadsTotal(event)).build();
		res.getLeads().stream().forEach(lead -> {
			completeLead(lead);
		});
		return res;
	}

	private void completeLead(LeadDTO lead) {
		lead.setEventLogs(findLeadEventLogs(lead.getId()));
		lead.setNextEvents(findNextEvents(lead.getId()));
	}

	public LeadDTO findLead(Long leadId) throws LeadsException {
		return LeadDTO.of(this.leadRepo.findById(leadId).orElseThrow( ()-> new LeadsException("Could not find Lead")));
	}

}
