package com.allscontracting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allscontracting.event.EmailListener;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.VendorFileListener;

@Configuration
public class EventConfig {

	@Autowired EmailListener emailListener;
	@Autowired VendorFileListener vendorFileListener;
	
	@Bean EventManager eventManager() {
		EventManager em = new EventManager();
		/**
		 * Register here all the event listeners
		 */
		em.subscribe(EventType.SCHEDULE_VISIT, this.emailListener);
		
		em.subscribe(EventType.SEND_PROPOSAL, this.emailListener);
		em.subscribe(EventType.SEND_INVOICE, this.emailListener);
		em.subscribe(EventType.LOAD_VENDOR_FILE, vendorFileListener);
		
		return em;
	}
	
}
