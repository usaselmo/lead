package com.allscontracting;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.dto.EventDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.event.EventDispatcher;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Person;
import com.allscontracting.model.User;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.EventoLogRepository;
import com.allscontracting.repo.InvitationRepo;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.PersonRepository;
import com.allscontracting.repo.UserRepository;
import com.allscontracting.service.LeadService;
import com.allscontracting.service.LogService;
import com.allscontracting.service.ReportService;
import com.allscontracting.service.mail.InvitationToBidMailProvider;

public class LeadServiceTest {

	private LeadRepository leadRepo = Mockito.mock(LeadRepository.class);
	private EventDispatcher eventDispatcher = Mockito.mock(EventDispatcher.class);
	private EventoLogRepository eventLogRepo = Mockito.mock(EventoLogRepository.class);
	private PersonRepository personRepo = Mockito.mock(PersonRepository.class);
	private UserRepository userRepo = Mockito.mock(UserRepository.class);
	private LogService logService = Mockito.mock(LogService.class);
	private CompanyRepository companyRepo = Mockito.mock(CompanyRepository.class);
	private InvitationToBidMailProvider invitationToBidMailProvider = Mockito.mock(InvitationToBidMailProvider.class);
	private InvitationRepo invitationRepo = Mockito.mock(InvitationRepo.class);
	private ReportService reportService = Mockito.mock(ReportService.class);
	private LeadService LeadService = new LeadService(leadRepo, eventDispatcher, eventLogRepo, personRepo, userRepo, logService, companyRepo, invitationToBidMailProvider, invitationRepo, reportService);
	private User user = Mockito.mock(User.class);
	private LeadDTO leadDTO = Mockito.mock(LeadDTO.class);

	@Test
	public void save_estimateId_notNull() throws Exception {
		when(leadDTO.getCompany()).thenReturn(CompanyDTO.builder().build());
		Mockito.when(leadRepo.save(Mockito.any(Lead.class))).thenReturn(Lead.builder().id(1L).client(null).build());
		doNothing().when(this.logService).newLeadCreated(Mockito.isA(String.class), Mockito.isA(Person.class), Mockito.isA(User.class));
		this.LeadService.save(leadDTO, user);
	}

	@Test
	public void findNextEvents_null() throws Exception {
		List<EventDTO> res = this.LeadService.findNextEvents(null);
		assertThat(res, notNullValue());
		assertThat(res.size(), is(0));
	}

	@Test
	public void findNextEvents_valorNegativo() throws Exception {
		List<EventDTO> res = this.LeadService.findNextEvents(-10L);
		assertThat(res, notNullValue());
		assertThat(res.size(), is(0));
	}

}
