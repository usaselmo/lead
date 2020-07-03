package com.allscontracting.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.LeadDTO;
import com.allscontracting.event.EventType;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.Converter;
import com.allscontracting.service.FileService;
import com.allscontracting.service.LeadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("leads")
public class LeadController {

	@Autowired LeadService leadService;
	@Autowired FileService fileService;
	@Autowired LeadRepository leadRepo;
	
	// /leads/id/estimator/id put
	
	@PutMapping("{leadId}/estimator/{estimatorId}")
	public LeadDTO assignEstimator(@PathVariable String leadId, @PathVariable String estimatorId) throws LeadsException {
		return leadService.assignEstimator(leadId, estimatorId); 
	}
	
	@GetMapping("/types")
	public List<String> getLeadTypes() {
		List<String> types= this.leadService.getLeadTypes();
		return types;
	}
	
	@PostMapping
	public Lead saveNewLead(@RequestBody Lead lead, @Autowired Authentication authentication) throws LeadsException {
		lead = this.leadService.saveNewLead(lead, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
		return lead;
	}

	@PostMapping(value = "{id}/addNote")
	public Lead addNewNote(@PathVariable String id, @RequestBody String note) throws LeadsException {
			Lead lead = this.leadService.addNewNote(id, note);
			return lead;
	}

	@GetMapping(value = "{id}/eventlogs")
	public List<EventLog> findEventLogs(@PathVariable String id) {
		try {
			List<EventLog> res = this.leadService.findLeadEventLogs(id);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@PostMapping(value = "{id}/schedulevisit")
	public ResponseEntity<Object> scheduleVisit(@PathVariable String id, @RequestBody String time, @Autowired Authentication authentication) throws LeadsException {
		try {
			Date visitDateTime = Converter.stringToDate(time, Converter.MM_dd_yy_hh_mm);
			this.leadService.scheduleAVisit(id, visitDateTime, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId()); 
			return ResponseEntity.ok().body(visitDateTime);
		} catch (ParseException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(value = "{id}/fireevent")
	public void fireEvent(@PathVariable String id, @RequestBody String event, @Autowired Authentication authentication) throws LeadsException {
		switch (EventType.reverse(event)) {
		case SCHEDULE_VISIT:
			this.leadService.scheduleAVisit(id, new Date(), ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
			break;
		default:
			this.leadService.fireEventToLead(event, id, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
			break;
		}
	}

	@GetMapping(value = "{id}/nextevents")
	public List<EventType> findNextEvents(@PathVariable String id) {
		try {
			List<EventType> res = this.leadService.findNextEvents(id);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "/search")
	public List<LeadDTO> search(@RequestParam String text) {
		try {
			return this.leadService.search(text);
		} catch (Exception e) {
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "")
	public List<LeadDTO> list(@RequestParam int pageRange, @RequestParam int lines, @RequestParam EventType eventType) {
		try {
			List<LeadDTO> leads = this.leadService.listLeads(pageRange, lines, eventType);
			return leads;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "/drop")
	public void drop(@Autowired Authentication authentication) {
		try {
			leadService.drop( ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
		} catch (Exception e) { 
			e.printStackTrace();
		}
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
