package com.allscontracting.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Person;
import com.allscontracting.model.User;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.PersonRepository;
import com.allscontracting.service.mail.Mail;
import com.allscontracting.service.mail.MailProviderSelector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

	private final PersonRepository personRepo;
	private final MailProviderSelector mailProviderSelector;
	private final LogService logg;
	private final CompanyRepository companyRepo;

	@Transactional
	public PersonDTO updatePerson(PersonDTO personDTO, User user) throws NumberFormatException, LeadsException {
		Person person = this.personRepo.findById(Long.valueOf(personDTO.getId())).orElseThrow(() -> new LeadsException("Person not found"));
		person.setAddress(personDTO.getAddress());
		person.setEmail(personDTO.getEmail());
		person.setName(personDTO.getName());
		person.setPhone(personDTO.getPhone());
		person.setCompany(personDTO.getCompany() != null ? this.companyRepo.findById(personDTO.getCompany().getId()).orElse(null) : null);
		logg.eventUpdated(Person.class, person.getId(), user, "");
		return PersonDTO.of(personRepo.save(person));
	}

	public void sendCantReachEmail(String leadId, User user, MailDTO emailDTO) throws Exception {
		Mail mail = MailDTO.to(emailDTO);
		this.mailProviderSelector.get(mail.getType()).getMailProvider(mail).onError((error) -> log.error("Error sending e-mail: " + error)).onSuccess(() -> {
			logg.eventCantReachEmailSent(leadId, emailDTO.getTo().get(0).getName(), user);
		}).send();
	}

	public void sendHiringDecisionEmail(String leadId, User user, MailDTO emailDTO) throws Exception {
		Mail mail = MailDTO.to(emailDTO);
		this.mailProviderSelector.get(mail.getType()).getMailProvider(mail).onError((error) -> log.error("Error sending e-mail: " + error))
		    .onSuccess(() -> logg.eventHiringDecisionEmailSent(leadId, emailDTO.getTo().get(0).getName(), user)).send();
	}

	public List<PersonDTO> findAll() {
		return PersonDTO.of(this.personRepo.findAll(Sort.by("name")));
	}

	public PersonDTO save(PersonDTO personDTO, User user) {
		Person person = PersonDTO.toPerson(personDTO);
		return PersonDTO.of(this.personRepo.save(person));
	}

}
