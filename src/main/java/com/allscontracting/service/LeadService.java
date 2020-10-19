package com.allscontracting.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.allscontracting.dto.EventDTO;
import com.allscontracting.dto.EventLogDTO;
import com.allscontracting.dto.FilterDTO;
import com.allscontracting.dto.InvitationDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.MailDTO;
import com.allscontracting.event.Event;
import com.allscontracting.event.EventDispatcher;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Media;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.User;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.InvitationRepo;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.PersonRepository;
import com.allscontracting.repo.UserRepository;
import com.allscontracting.service.mail.Mail;
import com.allscontracting.service.mail.MailProviderSelector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	private final MailProviderSelector mailProviderSelector;
	private final InvitationRepo invitationRepo;
	private final ReportService reportService;

	public List<EventDTO> findNextEvents(String leadId) {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElse(null);
		Event currentEvent = lead.getEvent();
		if (null == currentEvent)
			currentEvent = Event.BEGIN;
		return eventDispatcher.findNextEvents(currentEvent).stream().map(et -> EventDTO.of(et)).collect(Collectors.toList());
	}

	public List<EventLogDTO> findLeadEventLogs(String leadId) {
		return eventLogRepo.findEventLogs(Lead.class.getSimpleName(), leadId).stream().map(e -> EventLogDTO.of(e)).collect(Collectors.toList());
	}

	public List<Proposal> findProposals(String leadId) {
		return this.leadRepo.findProposals(Long.valueOf(leadId));
	}

	@Transactional
	public LeadDTO addNewNote(String leadId, String note) throws LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadId)).orElseThrow(() -> new LeadsException("Lead not found"));
		lead.addNote(note);
		lead = this.leadRepo.save(lead);
		LeadDTO leadDTO = LeadDTO.of(lead);
		completeLead(leadDTO);
		return leadDTO;
	}

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

	@Transactional
	public LeadDTO update(LeadDTO leadDTO, User user) throws NumberFormatException, LeadsException {
		Lead lead = this.leadRepo.findById(Long.valueOf(leadDTO.getId())).orElseThrow(() -> new LeadsException("Lead not found"));
		lead.setTitle(leadDTO.getTitle());
		lead.setCompany(
		    leadDTO.getCompany() == null || leadDTO.getCompany().getId() == null ? null : this.companyRepo.findById(leadDTO.getCompany().getId()).orElse(null));
		lead.setContact(leadDTO.getContact() == null || StringUtils.isEmpty(leadDTO.getContact().getId()) ? null
		    : this.personRepo.findById(Long.valueOf(leadDTO.getContact().getId())).orElse(null));
		lead.setClient(leadDTO.getClient() == null || StringUtils.isEmpty(leadDTO.getClient().getId()) ? null
		    : this.personRepo.findById(Long.valueOf(leadDTO.getClient().getId())).orElse(null));
		lead.setEstimator(leadDTO.getEstimator() == null || StringUtils.isEmpty(leadDTO.getEstimator().getId()) ? null
		    : this.userRepo.findById(Long.valueOf(leadDTO.getEstimator().getId())).orElse(null));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit(), Converter.MM_dd_yyyy_hh_mm));
		lead.setDueDate(Converter.convertToDate(leadDTO.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
		lead.setAddress(leadDTO.getAddress());
		lead.setDescription(leadDTO.getDescription());
		leadDTO = LeadDTO.of(this.leadRepo.save(lead));
		completeLead(leadDTO);
		logg.event(Lead.class, lead.getId(), Event.UPDATE, user, "Lead has been changed");
		return leadDTO;
	}

	public List<String> getLeadTypes() {
		return this.leadRepo.findByType();
	}

	public long getLeadsTotal(List<LeadDTO> leads, Event event) throws LeadsException {
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

	public LeadEntity list(FilterDTO filter) throws LeadsException {
		List<LeadDTO> leads = listLeads(filter);
		long leadsTotalPrice = leads.stream().mapToLong(l -> l.getPrice()).sum();
		LeadEntity res = LeadEntity.builder().leads(leads).leadsTotalPrice(leadsTotalPrice).leadTypes(getLeadTypes())
		    .events(Stream.of(Event.values()).filter(e -> e.isShowInMenu() == true).map(et -> EventDTO.of(et)).collect(Collectors.toList()))
		    .totalLeads(getLeadsTotal(leads, EventDTO.to(filter.getEvent()))).build();
		res.getLeads().stream().forEach(lead -> {
			completeLead(lead);
		});
		return res;
	}

	private void completeLead(LeadDTO lead) {
		lead.setEventLogs(findLeadEventLogs(lead.getId()));
		lead.setNextEvents(findNextEvents(lead.getId()));
	}

	private List<LeadDTO> listLeads(FilterDTO filter) throws LeadsException {
		PageRequest pageable = PageRequest.of(filter.getPageRange() < 0 ? 0 : filter.getPageRange(), filter.getLines(), new Sort(Sort.Direction.DESC, "date"));
		if (filter.getEvent()==null && StringUtils.isEmpty(filter.getSearchText()))// nada
			return this.leadRepo.findAll(pageable).stream().distinct().map(l -> LeadDTO.of(l)).collect(Collectors.toList());
		else {
			List<Event> events = (filter.getEvent() == null) ? Arrays.asList(Event.values()) : Arrays.asList(EventDTO.to(filter.getEvent()));
			if (StringUtils.isEmpty(filter.getSearchText()))
				return this.leadRepo.search(events, pageable).stream().map(l -> LeadDTO.of(l)).collect(Collectors.toList());
			else
				return leadRepo.search(filter.getSearchText(), events, pageable).stream().map(l -> LeadDTO.of(l)).collect(Collectors.toList());
		}
	}

	public LeadDTO findLead(Long leadId) throws LeadsException {
		LeadDTO leadDTO = LeadDTO.of(this.leadRepo.findById(leadId).orElseThrow(() -> new LeadsException("Could not find Lead")));
		completeLead(leadDTO);
		return leadDTO;
	}

	@Transactional
	public void sendInvitationByEmail(InvitationDTO invitationDTO, User user, MailDTO mailDTO) throws Exception {
		Invitation invitation = this.invitationRepo.findById(invitationDTO.getId()).orElseThrow(() -> new LeadsException("Could not find Invitation"));
		Mail mail = MailDTO.to(mailDTO);
		this.mailProviderSelector.get(Mail.TYPE.INVITATION_TO_BID).getMailProvider(mail, invitation).onError((error) -> {
			log.error(error);
		}).onSuccess(() -> {
			invitation.setEmailed((invitation.getEmailed() == null) ? 1L : invitation.getEmailed() + 1L);
			this.invitationRepo.save(invitation);
			logg.event(Lead.class, invitation.getLead().getId(), Event.EMAIL_SENT, user,
			    "Invitation #" + invitation.getId() + " e-mailed to " + invitation.getContact().getName() + " - " + user.getName());
		}).send();
	}

	@Transactional
	public InvitationDTO markAsEmailed(Long invitationId, User user) throws LeadsException {
		Invitation invitation = this.invitationRepo.findById(invitationId).orElseThrow(() -> new LeadsException("Could not find Invitation"));
		invitation.setEmailed(invitation.getEmailed() + 1L);
		invitation = this.invitationRepo.save(invitation);
		logg.event(Lead.class, invitation.getLead().getId(), Event.EMAIL_SENT, user,
		    "Invitation #" + invitation.getId() + " e-mailed to " + invitation.getContact().getName() + " - " + user.getName());
		return InvitationDTO.of(invitation);
	}

	public void getInvitationAsPdfStream(HttpServletResponse response, Long invitationId, Long proposalId) throws IOException, LeadsException {
		Invitation invitation = this.invitationRepo.findById(invitationId).orElseThrow(() -> new LeadsException("Could not find Invitation"));
		Media proposal = invitation.getProposals().stream().filter(prop -> prop.getId().equals(proposalId)).findFirst()
		    .orElseThrow(() -> new LeadsException("Could not find Proposal"));
		this.reportService.getFileAsPdfStream(response, proposal.getName(), proposal.getContent());
	}

}
