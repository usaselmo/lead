package com.allscontracting.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

	private Map<EventType, List<DomainListener>> listeners = new HashMap<>();

	public void notifyAllListeners(DomainEvent event) {
		this.listeners.get(event.getEventType()).forEach(listener->listener.update(event));
	}

	public void subscribe(EventType event, DomainListener emailListener) {
		if(this.listeners.get(event)==null)
			this.listeners.put(event, new ArrayList<DomainListener>());
		this.listeners.get(event).add(emailListener);
	}

}
