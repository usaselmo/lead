package com.allscontracting.event;

import java.util.Date;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class VendorFileLoadedEvent implements DomainEvent {

	private Lead leadLoaded;
	private Vendor vendor;
	private final EventType eventType = EventType.LOAD_VENDOR_FILE;
	private final Date eventTime = new Date();
	private final String objectName = Lead.class.getSimpleName();

	@Override
	public String getObjectId() {
		return leadLoaded.getId();
	}

	@Override
	public String getMessage() {
		return null;
	}

}
