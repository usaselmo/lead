package com.allscontracting.event;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.model.Lead;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class LeadEventManager {

	List<DomainEventListener<Lead>> listeners = new ArrayList<>();
	@Autowired MailListener mailListener;

	@PostConstruct
	void init() {
		this.listeners.add(mailListener);
	}

	public void subscribe(Event event, DomainEventListener<Lead> listener) {
		listeners.add(listener);
	}

	public void unsubscribe(Event event, DomainEventListener<Lead> listener) {
		listeners.remove(listener);
	}

	public void notifyAllListeners(Event event, Lead lead) {
		this.listeners.forEach(listener -> listener.update(event, lead));
	}

}
