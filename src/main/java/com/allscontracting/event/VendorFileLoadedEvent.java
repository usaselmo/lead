package com.allscontracting.event;

import java.util.List;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VendorFileLoadedEvent implements DomainEvent{

	private List<Lead> leadsLoaded;
	private Vendor vendor;
	
	@Override
	public EventType getEventType() {
		return EventType.VENDOR_FILE_LOADED;
	}

}
