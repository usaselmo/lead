package com.allscontracting;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.service.LeadService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired LeadService leadService;
	@Autowired EventManager eventManager;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void init() throws Exception {
		assertNotNull(this.leadService);
	}
	
	@Test
	public void changeLeadEvent() throws Exception {
		LocalDateTime dateTime;
		Lead lead;
		Client client;
		this.leadService.scheduleAVisit("", new Date());
	}
	
	@Test
	@Rollback
	@Ignore
	public void testEvent() throws Exception {
		this.eventManager.notifyAllListeners(new VisitScheduledEvent(Lead.builder().id("sdfsd").build(), Client.builder().id(125L).build(), new Date()));
	}
	
}
