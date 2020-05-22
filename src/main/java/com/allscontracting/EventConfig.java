package com.allscontracting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allscontracting.event.EmailListener;
import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;

@Configuration
public class EventConfig {

	@Autowired EmailListener emailListener;
	
	@Bean EventManager eventManager() {
		EventManager em = new EventManager();
		/**
		 * Register here all the event listeners
		 */
		em.subscribe(EventType.ESTIMATE_SCHEDULED, this.emailListener);
		return em;
	}
	
}
