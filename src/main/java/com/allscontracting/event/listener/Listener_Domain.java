package com.allscontracting.event.listener;

import com.allscontracting.event.DomainEvent;

public interface DomainListener {
	void update(DomainEvent domainEvent);
}
