package com.allscontracting.event;

import java.time.LocalDateTime;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EstimateScheduledEvent implements DomainEvent {

	private Lead lead;
	private Client client;
	private LocalDateTime time;
	
	@Override
	public EventType getEventType() {
		return EventType.ESTIMATE_SCHEDULED;
	}

}
