package com.allscontracting.event;

import java.util.EventListener;

public interface DomainEventListener<T> extends EventListener{

	void update(Event event, T arg);
	
}
