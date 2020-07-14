package com.allscontracting.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.EventTypeDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.event.EventType;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.FileService;
import com.allscontracting.service.LeadService;

@RestController
@RequestMapping("leads")
public class LeadController {

	@Autowired LeadService leadService;
	@Autowired FileService fileService;
	@Autowired LeadRepository leadRepo;
	
	@GetMapping("eventtypes")
	public LeadEntity findEventTypes(){
		return LeadEntity.builder().eventTypes(Stream.of(EventType.values()).map(et->EventTypeDTO.of(et)).collect(Collectors.toList())).build();
	}
	
	@PutMapping("{leadId}/estimator/{estimatorId}")
	public LeadEntity assignToEstimator(@PathVariable String leadId, @PathVariable String estimatorId, @Autowired Authentication authentication) throws LeadsException {
		return LeadEntity.builder().lead(leadService.assignEstimator(leadId, estimatorId, ((LeadUserDetails)authentication.getPrincipal()).getUser())).build(); 
	}
	
	@GetMapping("/types")
	public LeadEntity getLeadTypes() {
		return LeadEntity.builder().leadTypes(this.leadService.getLeadTypes()).build();
	}
	
	@PostMapping
	public LeadEntity saveNewLead(@RequestBody LeadDTO leadDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().lead(leadService.saveNewLead(leadDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build();
		} catch (LeadsException e) {
			return LeadEntity.builder().lead(leadDTO).build().addErrorMessage(e.getMessage());
		}
	}

	@PostMapping(value = "{id}/addNote")
	public LeadEntity addNewNote(@PathVariable String id, @RequestBody String note) throws LeadsException {
		return LeadEntity.builder().lead(leadService.addNewNote(id, note)).build();
	}

	@GetMapping(value = "{id}/eventlogs")
	public LeadEntity findEventLogs(@PathVariable String id) {
		return LeadEntity.builder().eventLogs(leadService.findLeadEventLogs(id)).build();
	}

	@PostMapping(value = "{id}/schedulevisit")
	public LeadEntity scheduleVisit(@PathVariable String id, @RequestBody String time, @Autowired Authentication authentication) throws LeadsException, ParseException {
		LeadDTO res = leadService.scheduleAVisit(id, time, ((LeadUserDetails)authentication.getPrincipal()).getUser()); 
		return LeadEntity.builder().lead(res).build();
	}

	@PostMapping(value = "{id}/fireevent")
	public void fireEvent(@PathVariable String id, @RequestBody EventTypeDTO event, @Autowired Authentication authentication) throws LeadsException {
		this.leadService.fireEventToLead(event.getName(), id, ((LeadUserDetails)authentication.getPrincipal()).getUser());
	}

	@GetMapping(value = "{id}/nextevents")
	public LeadEntity findNextEvents(@PathVariable String id) throws LeadsException{
		return LeadEntity.builder().nextEvents(leadService.findNextEvents(id)).build();
	}

	@GetMapping(value = "/search")
	public LeadEntity search(@RequestParam String text) {
		return LeadEntity.builder().leads(leadService.search(text)).build();
	}

	@GetMapping(value = "")
	public LeadEntity list(@RequestParam int pageRange, @RequestParam int lines, @RequestParam EventType eventType) throws Exception {
		List<LeadDTO> res = leadService.listLeads(pageRange, lines, eventType);
		long leadsTotalPrice = res.stream().mapToLong(l->l.getPrice()).sum();
		return LeadEntity.builder().leads(res).leadsTotalPrice(leadsTotalPrice).build();
	}

	@GetMapping(value = "/total")
	public Long findTotal(@RequestParam EventType eventType) {
		try {
			long res = leadService.getLeadsTotal(eventType);
			return res;
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
}
