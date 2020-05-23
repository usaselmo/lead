package com.allscontracting.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventManager {

	private Map<EventType, List<DomainListener>> listeners = new HashMap<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public void notifyAllListeners(DomainEvent event) {
		executor.execute( ()->{
			this.listeners.get(event.getEventType()).forEach(listener->listener.update(event));
		});
		executor.shutdown();
	}

	public void subscribe(EventType event, DomainListener emailListener) {
		if(this.listeners.get(event)==null)
			this.listeners.put(event, new ArrayList<DomainListener>());
		this.listeners.get(event).add(emailListener);
	}

}
