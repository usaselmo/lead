package com.allscontracting.controller;

import java.io.IOException;
import java.util.List;

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
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.PersonService;

@RestController
@RequestMapping("/persons")
public class PersonController { 

	@Autowired PersonService personService;
	
	@GetMapping("")
	public List<PersonDTO> list() {
		List<PersonDTO> res = this.personService.findAll();
		return res;
	}

	@GetMapping("{personId}/leads/{leadId}/cantreach")
	public void sendCantReachEmail(@PathVariable String personId, @PathVariable String leadId, @Autowired Authentication authentication) throws IOException, NumberFormatException, LeadsException {
		personService.sendCantReachEmail(personId, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser());
	}

	@GetMapping("{personId}/leads/{leadId}/hiringdecision")
	public void sendHiringDecisionEmail(@PathVariable String personId, @PathVariable String leadId, @Autowired Authentication authentication) throws IOException, NumberFormatException, LeadsException {
		personService.sendHiringDecisionEmail(personId, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser());
	}
	
	@PutMapping("")
	public PersonDTO updatePerson(@RequestBody PersonDTO personDTO, @Autowired Authentication authentication) throws LeadsException {
		return this.personService.updatePerson(personDTO, ((LeadUserDetails)authentication.getPrincipal()).getUser());
	}
	
	@PostMapping("")
	public LeadEntity save(@RequestBody PersonDTO personDTO, @Autowired Authentication authentication) throws LeadsException {
		try {
			return LeadEntity.builder().person(this.personService.save(personDTO, ((LeadUserDetails)authentication.getPrincipal()).getUser())).build().addSuccessMessage("Person saved.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not save.");
		}
	}
	
	
	
	
	
	
	

	
	/*
	 * @GetMapping("search") public List<PersonDTO> searchPerson(@RequestParam
	 * String name) { List<PersonDTO> res = this.personService.findByName(name);
	 * return res; }
	 */
	
}
