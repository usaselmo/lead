package com.allscontracting.controller;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.event.EventType;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Lead;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.service.Converter;
import com.allscontracting.service.FileService;
import com.allscontracting.service.LeadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("leads")
public class LeadController {

	@Autowired
	LeadService leadService;
	@Autowired FileService fileService;
	
	@Autowired LeadRepository leadRepo;

	@GetMapping(value = "{id}/eventlogs")
	public List<EventLog> findEventLogs(@PathVariable String id) {
		try {
			List<EventLog> res = this.leadService.findEventLogs(id);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@PostMapping(value = "{id}/schedulevisit")
	public ResponseEntity<Object> scheduleVisit(@PathVariable String id, @RequestBody String time) {
		try {
			Date visitDateTime = Converter.stringToDate(time, Converter.MM_dd_yy_hh_mm);
			this.leadService.scheduleAVisit(id, visitDateTime); 
			return ResponseEntity.ok().body(visitDateTime);
		} catch (ParseException e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(value = "{id}/fireevent")
	public void fireEvent(@PathVariable String id, @RequestBody String event) {
		switch (EventType.valueOf(event)) {
		case SCHEDULE_VISIT:
			this.leadService.scheduleAVisit(id, new Date());
			break;
		default:
			this.leadService.fireEvent(event, id);
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

	@GetMapping(value = "")
	public List<Lead> list(@RequestParam int pageRange, @RequestParam int lines, @RequestParam EventType eventType) {
		try {
			List<Lead> res = this.leadService.listLeads(pageRange, lines, eventType);
			return res;
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage());
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "/drop")
	public void drop() {
		try {
			leadService.drop();
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
