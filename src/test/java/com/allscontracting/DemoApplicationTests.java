package com.allscontracting;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.service.LeadService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired LeadService leadService;

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

}
