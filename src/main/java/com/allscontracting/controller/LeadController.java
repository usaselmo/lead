package com.allscontracting.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.allscontracting.dto.EventLogDTO;
import com.allscontracting.dto.EventTypeDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.event.EventType;
import com.allscontracting.exception.LeadsException;
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
	
	@GetMapping("eventtypes")
	public List<EventTypeDTO> findEventTypes(){
		return Stream.of(EventType.values()).map(et->EventTypeDTO.of(et)).collect(Collectors.toList());
	}
	
	@PutMapping("{leadId}/estimator/{estimatorId}")
	public LeadDTO assignEstimator(@PathVariable String leadId, @PathVariable String estimatorId, @Autowired Authentication authentication) throws LeadsException {
		return leadService.assignEstimator(leadId, estimatorId, ((LeadUserDetails)authentication.getPrincipal()).getUser()); 
	}
	
	@GetMapping("/types")
	public List<String> getLeadTypes() {
		List<String> types= this.leadService.getLeadTypes();
		return types;
	}
	
	@PostMapping
	public LeadDTO saveNewLead(@RequestBody LeadDTO leadDTO, @Autowired Authentication authentication) throws LeadsException {
		leadDTO = this.leadService.saveNewLead(leadDTO, ((LeadUserDetails)authentication.getPrincipal()).getUser());
		return leadDTO;
	}

	@PostMapping(value = "{id}/addNote")
	public LeadDTO addNewNote(@PathVariable String id, @RequestBody String note) throws LeadsException {
			LeadDTO lead = this.leadService.addNewNote(id, note);
			return lead;
	}

	@GetMapping(value = "{id}/eventlogs")
	public List<EventLogDTO> findEventLogs(@PathVariable String id) {
		try {
			List<EventLogDTO> res = this.leadService.findLeadEventLogs(id);
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
			this.leadService.scheduleAVisit(id, visitDateTime, ((LeadUserDetails)authentication.getPrincipal()).getUser()); 
			return ResponseEntity.ok().body(visitDateTime);
		} catch (ParseException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(value = "{id}/fireevent")
	public void fireEvent(@PathVariable String id, @RequestBody EventTypeDTO event, @Autowired Authentication authentication) throws LeadsException {
		switch (EventType.valueOf(event.getName())) {
		case SCHEDULE_VISIT:
			this.leadService.scheduleAVisit(id, new Date(), ((LeadUserDetails)authentication.getPrincipal()).getUser());
			break;
		default:
			this.leadService.fireEventToLead(event.getName(), id, ((LeadUserDetails)authentication.getPrincipal()).getUser());
			break;
		}
	}

	@GetMapping(value = "{id}/nextevents")
	public List<EventTypeDTO> findNextEvents(@PathVariable String id) {
		try {
			List<EventTypeDTO> res = this.leadService.findNextEvents(id);
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
