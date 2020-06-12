package com.allscontracting.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.allscontracting.event.listener.DomainListener;

public class EventManager {

	private Map<String, List<DomainListener>> listeners = new HashMap<>();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	public void notifyAllListeners(DomainEvent event) {
		executor.execute(() -> {
			this.listeners.get(event.getEventType().toString()).forEach(listener -> listener.update(event));
		});
	}

	public void subscribe(String key, DomainListener emailListener) {
		if (this.listeners.get(key) == null)
			this.listeners.put(key, new ArrayList<DomainListener>());
		this.listeners.get(key).add(emailListener);
	}

}
