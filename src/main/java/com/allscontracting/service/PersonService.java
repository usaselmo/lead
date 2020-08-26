package com.allscontracting.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.PersonDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Person;
import com.allscontracting.model.User;
import com.allscontracting.repo.PersonRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PersonService {

	@Autowired PersonRepository personRepo;
	@Autowired MailService mailService;
	@Autowired LogService logg;
	
	@Transactional
	public PersonDTO updatePerson(PersonDTO personDTO, User user) throws NumberFormatException, LeadsException {
		Person person = this.personRepo.findById(Long.valueOf(personDTO.getId())).orElseThrow(()->new LeadsException("Person not found"));
		person.setAddress(personDTO.getAddress());
		person.setEmail(personDTO.getEmail());
		person.setName(personDTO.getName());
		person.setPhone(personDTO.getPhone());
		logg.eventUpdated(Person.class, person.getId(), user, "");
		return PersonDTO.of(personRepo.save(person));
	}

	public List<PersonDTO> findByName(String name) {
		return this.personRepo.findLikeName(name).stream().map(c->PersonDTO.of(c)).collect(Collectors.toList());
	}

	public void sendCantReachEmail(String id, String leadId, User user) throws IOException, NumberFormatException, LeadsException {
		Person person = this.personRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Person not found"));
		this.mailService.sendCantReachEmail(person).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		logg.eventCantReachEmailSent(leadId, person, user);
	}

	public void sendHiringDecisionEmail(String id, String leadId, User user) throws IOException, NumberFormatException, LeadsException {
		Person person = this.personRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Person not found"));
		this.mailService.sendHiringDecisionEmail(person).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		logg.eventHiringDecisionEmailSent(leadId, person, user); 
	}

	public List<PersonDTO> findAll() {
		return PersonDTO.of(this.personRepo.findAll(Sort.by("name")));
	}
	
}
