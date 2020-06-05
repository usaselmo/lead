package com.allscontracting;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.LeadStatusChangeEvent;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.repo.ClientRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.tradutor.impl.EmailLeadTranslatorImpl;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Main.class, arguments);
	}

	@Autowired private LeadRepository leadRepo;
	@Autowired private ClientRepository clientRepo;
	@Autowired private EventManager EventManager;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		//registerLead();
	}

	private void registerLead() {
		Lead lead = Lead.builder()
			.client(Client.builder()
					.address("5603 Meridian Hill Place, Burke, VA")
					.email("")
					.name("Jason Johnson")
					.phone("5712751435")
					.build())
			.date(new Date())
			.description("New driveway and repairs or replace sidewalk and front stoop") 
			.event(EventType.BEGIN) 
			.fee(BigDecimal.ZERO) 
			.type("Install Driveway, Patio and Walkway from Email (Form Processor)") 
			.vendor(Vendor.EMAIL)
			.build();
		if(lead.getClient().getId() !=null && clientRepo.exists(lead.getClient().getId()))
			lead.setClient(this.clientRepo.findOne(lead.getClient().getId()));
		else
			lead.setClient(clientRepo.save(lead.getClient()));  
		lead.setId(EmailLeadTranslatorImpl.defineId(lead.getClient().getEmail(), lead.getClient().getPhone()));
		if(!leadRepo.exists(lead.getId())) {
			leadRepo.save(lead);
			this.EventManager.notifyAllListeners(new LeadStatusChangeEvent(EventType.BEGIN, lead.getId()));
		}
	}

}
