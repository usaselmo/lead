package com.allscontracting.controller;

import java.text.ParseException;
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

import com.allscontracting.dto.EventDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.LeadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("leads")
@RequiredArgsConstructor
public class LeadController {

	private final LeadService leadService;

	@GetMapping("eventtypes")
	public LeadEntity findEventTypes() {
		try {
			return LeadEntity.builder().events(Stream.of(Event.values()).filter(e -> e.isShowInMenu() == true).map(et -> EventDTO.of(et)).collect(Collectors.toList())).build();
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@PutMapping("{leadId}/estimator/{estimatorId}")
	public LeadEntity assignToEstimator(@PathVariable String leadId, @PathVariable String estimatorId, @Autowired Authentication authentication) throws LeadsException {
		try {
			return LeadEntity.builder().lead(leadService.assignEstimator(leadId, estimatorId, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@GetMapping("/types")
	public LeadEntity getLeadTypes() {
		try {
			return LeadEntity.builder().leadTypes(this.leadService.getLeadTypes()).build();
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@PostMapping(value = "{id}/addNote")
	public LeadEntity addNewNote(@PathVariable String id, @RequestBody String note) throws LeadsException {
		try {
			LeadDTO lead = leadService.addNewNote(id, note);
			return LeadEntity.builder().lead(lead).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@PostMapping(value = "{id}/schedulevisit")
	public LeadEntity scheduleVisit(@PathVariable String id, @RequestBody String time, @Autowired Authentication authentication) throws LeadsException, ParseException {
		try {
			LeadDTO res = leadService.scheduleAVisit(id, time, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().lead(res).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@GetMapping(value = "/search")
	public LeadEntity search(@RequestParam String text) {
		try {
			return LeadEntity.builder().leads(leadService.search(text)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@PostMapping
	public LeadEntity save(@RequestBody LeadDTO leadDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().lead(leadService.save(leadDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build().addSuccessMessage("Lead Created.");
		} catch (NumberFormatException | LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Unexpected error.");
		}
	}

	@PutMapping
	public LeadEntity update(@RequestBody LeadDTO leadDTO) {
		try {
			return LeadEntity.builder().lead(leadService.update(leadDTO)).build().addSuccessMessage("Lead updated.");
		} catch (NumberFormatException | LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Unexpected error.");
		}
	}

	@PostMapping(value = "{id}/fireevent")
	public LeadEntity fireEvent(@PathVariable String id, @RequestBody EventDTO event, @Autowired Authentication authentication) throws LeadsException {
		try {
			LeadDTO lead = leadService.fireEvent(id, Event.reverse(event.getName()), ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().lead(lead).build().addSuccessMessage("Event fired.");
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@GetMapping(value = "")
	public LeadEntity list(@RequestParam int pageRange, @RequestParam int lines, @RequestParam Event event, @RequestParam String text) throws Exception {
		try {
			LeadEntity res = this.leadService.list(pageRange, lines, event, text);
			return res;
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Erro inesperado.");
		}
	}

	@GetMapping(value = "/total")
	public Long findTotal(@RequestParam Event eventType) {
		try {
			long res = leadService.getLeadsTotal(eventType);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
