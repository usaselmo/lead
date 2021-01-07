package com.allscontracting.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.allscontracting.event.Event;
import com.allscontracting.model.EventLog;
import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Person;
import com.allscontracting.model.User;
import com.allscontracting.repo.EventoLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

	private final EventoLogRepository eventLogRepo;

	@Transactional
	private void fireEvent(Class<?> clazz, String objectId, String eventType, User user, String message) {
		eventLogRepo.save(new EventLog(clazz.getSimpleName(), objectId, eventType, new Date(), user, message));
	}

	@Transactional
	public void event(Class<?> object, String id, String eventDescription, User user, String msg) {
		fireEvent(object, id, eventDescription, user, msg);
	}

	@Transactional
	public void event(Class<?> object, String id, Event eventType, User user) {
		fireEvent(object, id, eventType.getStatus(), user, "");
	}

	@Transactional
	public void event(Class<?> object, Long id, Event eventType, User user, String message) {
		fireEvent(object, String.valueOf(id), eventType.getStatus(), user, message);
	}

	@Transactional
	public void event(Class<?> object, Long id, Event eventType, User user) {
		fireEvent(object, String.valueOf(id), eventType.getStatus(), user, "");
	}

	@Transactional
	public void eventUpdated(Class<?> object, String id, User user, String message) {
		fireEvent(object, id, Event.UPDATE.getStatus(), user, message);
	}

	@Transactional
	public void eventUpdated(Class<?> object, Long id, User user, String message) {
		fireEvent(object, String.valueOf(id), Event.UPDATE.getStatus(), user, message);
	}

	@Transactional
	public void eventCantReachEmailSent(String leadId, String clientName, User user) {
		fireEvent(Lead.class, leadId, Event.EMAIL_SENT.getStatus(), user, "Can't Reach E-mail sent to " + clientName);
	}

	@Transactional
	public void eventHiringDecisionEmailSent(String leadId, String clientName, User user) {
		fireEvent(Lead.class, leadId, Event.EMAIL_SENT.getStatus(), user, "Hiring Decision Question E-mailed to " + clientName);
	}

	@Transactional
	public void newLeadCreated(String leadId, Person person, User user) {
		fireEvent(Lead.class, leadId, Event.CREATE.getStatus(), user, "New Lead created. ID: " + leadId);
	}

	@Transactional
	public void newInvitationCreated(Long leadId, Invitation inv, User user) {
		fireEvent(Lead.class, String.valueOf(leadId), Event.CREATE.getStatus(), user, "New Invitation #" + inv.getId() + " created to " + inv.getCompany().getName());
	}

}
