package com.allscontracting.event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.repo.ClientJpaRepository;
import com.allscontracting.repo.LeadJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VendorFileListener implements DomainListener {

	@Autowired
	LeadJpaRepository leadRepo;
	@Autowired
	ClientJpaRepository clientRepo;
	final ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void update(DomainEvent domainEvent) {
		log.info("Vendor File Listener fired....{}", domainEvent);
		try {
			if (domainEvent.getEventType().equals(EventType.LOAD_VENDOR_FILE)) {
				this.executor.execute(() -> {
					VendorFileLoadedEvent event = (VendorFileLoadedEvent) domainEvent;
					Lead lead = event.getLeadLoaded();
					verifyPhoneNumber(lead.getClient());
					log.info("Vendor File Listener fixed client's phone number positions.");
				});
				executor.shutdown();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Transactional
	private void verifyPhoneNumber(Client clientFromEvent) {
		try {
			Client client = this.clientRepo.findOne(clientFromEvent.getId());
			if(client==null) {
				final String p1 = clientFromEvent.getPhone();
				final String p2 = clientFromEvent.getCellPhone();
				if (StringUtils.isEmpty(p1) && !StringUtils.isEmpty(p2)) {
					client.setPhone(p2);
					client.setPhone(p1);
				} else if (!StringUtils.isEmpty(p1) && StringUtils.isEmpty(p2)) {
					client.setPhone(p1);
					client.setCellPhone(p2);
				}
				this.clientRepo.save(client);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}
