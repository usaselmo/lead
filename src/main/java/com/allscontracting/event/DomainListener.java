package com.allscontracting.event;

public interface DomainListener<T> {
	void doAction(T domainEvent);
}
