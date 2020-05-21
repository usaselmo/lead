package com.allscontracting;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.event.Event;
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
	public void testName() throws Exception {
		this.leadService.registryEvent(Lead.builder().id("isifido").build(), Event.BEGIN_WORK);
	}

}
