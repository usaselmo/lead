package com.allscontracting.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.PersonDTO;
import com.allscontracting.model.Person;
import com.allscontracting.repo.PersonRepository;
import com.allscontracting.service.mail.EmailMarketingProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class EmailCampainService {

	private static final int INTERVAL = 1000 * 60 * 15; // every 15 minutes

	private static boolean LIGADO = false;
	private static int REGISTRY = 0;

	private final EmailMarketingProvider emailService;
	private final PersonRepository personRepo;

	public boolean ligaDesliga() {
		LIGADO = !LIGADO;
		return LIGADO;
	}

	@Scheduled(fixedRate = INTERVAL)
	public final void emailMarketing() throws IOException {
		MailDTO nextMail = this.getNextEmail();
		if (LIGADO && nextMail != null) {
			emailService.email(nextMail, Collections.emptyList());
			log.info("Email campain sent to: {}", nextMail);
		} else {
			log.info("No more emails to send campain to.");
		}
	}

	private MailDTO getNextEmail() {
		Person res = this.personRepo.findAll(PageRequest.of(REGISTRY, 1)).get().findFirst().orElse(null);
		if (res == null)
			return null;
		else 
			REGISTRY = REGISTRY + 1 ;
		return new MailDTO(Arrays.asList(PersonDTO.of(res)), Collections.emptyList(), "", Collections.emptyList(), null, null);
	}

}
