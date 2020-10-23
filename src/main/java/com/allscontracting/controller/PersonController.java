package com.allscontracting.controller;

import java.util.List;

import javax.websocket.server.PathParam;

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

import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.PersonService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonController {

	private final PersonService personService;

	@GetMapping("")
	public List<PersonDTO> list() {
		List<PersonDTO> res = this.personService.findAll();
		return res;
	}

	@GetMapping("/search/{text}")
	public List<PersonDTO> search(@PathVariable String text) {
		List<PersonDTO> res = this.personService.search(text);
		return res;
	}

	@PostMapping("{personId}/leads/{leadId}/cantreach")
	public void sendCantReachEmail(@PathVariable String leadId, @RequestBody MailDTO mailDTO, @Autowired Authentication authentication) throws Exception {
		personService.sendCantReachEmail(leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser(), mailDTO);
	}

	@PostMapping("{personId}/leads/{leadId}/hiringdecision")
	public void sendHiringDecisionEmail(@PathVariable String personId, @PathVariable String leadId, @RequestBody MailDTO mailDTO,
	    @Autowired Authentication authentication) throws Exception {
		personService.sendHiringDecisionEmail(leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser(), mailDTO);
	}

	@PutMapping("")
	public LeadEntity updatePerson(@RequestBody PersonDTO personDTO, @Autowired Authentication authentication) throws LeadsException {
		PersonDTO res = this.personService.updatePerson(personDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser());
		return LeadEntity.builder().person(res).build();
	}

	@PostMapping("")
	public LeadEntity save(@RequestBody PersonDTO personDTO, @Autowired Authentication authentication) throws LeadsException {
		try {
			return LeadEntity.builder().person(this.personService.save(personDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build()
			    .addSuccessMessage("Person saved.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not save.");
		}
	}

}
