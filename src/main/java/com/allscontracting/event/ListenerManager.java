package com.allscontracting.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ListenerManager {

	Map<StateEnum, List<DomainListener<DomainEvent>>> listeners = new HashMap<>();
	@Autowired
	EmailListener estimateScheduledListener;

	public <T extends DomainListener<DomainEvent>> void subscribe(StateEnum event, T listener) {
		if(this.listeners.get(event)== null)
			this.listeners.put(event, new ArrayList<>());
		this.listeners.get(event).add(listener);
	}

	public void unsubscribe(StateEnum event, DomainListener<DomainEvent> listener) {
		this.listeners.get(event).remove(listener);
	}

	public  void notifyAllListeners(DomainEvent event) {
		this.listeners.get(event.getStateEnum()).forEach(listener -> listener.doAction(event));
	}

}
