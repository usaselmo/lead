package com.allscontracting;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.listener.EmailListener;
import com.allscontracting.event.listener.EventLogListener;
import com.allscontracting.event.listener.VendorFileListener;

@Configuration
public class EventConfig {

	@Autowired EmailListener emailListener;
	@Autowired VendorFileListener vendorFileListener;
	@Autowired EventLogListener eventLogListener;
	
	@Bean EventManager eventManager() {
		EventManager em = new EventManager();
		/**
		 * Register here all the event listeners
		 */
		em.subscribe(EventType.SCHEDULE_VISIT, this.emailListener);
		em.subscribe(EventType.SEND_PROPOSAL, this.emailListener);
		em.subscribe(EventType.SEND_INVOICE, this.emailListener);
		em.subscribe(EventType.LOAD_VENDOR_FILE, vendorFileListener);
		Stream.of(EventType.values()).forEach(et->em.subscribe(et, eventLogListener));
		
		return em;
		
	}
	
}
