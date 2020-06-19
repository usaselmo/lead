package com.allscontracting.service;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.repo.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService {

	@Autowired ClientRepository clientRepo;
	@Autowired MailService mailService;
	@Autowired EventManager eventManager;
	
	@Transactional
	public Client updateClient(Client client, Long userId) {
		Client localClient = this.clientRepo.findOne(client.getId());
		localClient.setAddress(client.getAddress());
		localClient.setEmail(client.getEmail());
		localClient.setName(client.getName());
		localClient.setPhone(client.getPhone());
		this.eventManager.notifyAllListeners(
				new AuditEvent(Client.class.getSimpleName(), String.valueOf(localClient.getId()), "Client updated: " + localClient.toString(), userId)
				);
		return this.clientRepo.save(localClient);
	}

	public List<Client> findByName(String name) {
		return this.clientRepo.findLikeName(name);
	}

	public void sendCantReachEmail(String id, String leadId, Long userId) throws IOException {
		Client client = this.clientRepo.findOne(Long.valueOf(id));
		this.mailService.sendCantReachEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Can't Reach E-mail sent to " + client.getName(), userId));
	}

	public void sendHiringDecisionEmail(String id, String leadId, Long userId) throws IOException {
		Client client = this.clientRepo.findOne(Long.valueOf(id));
		this.mailService.sendHiringDecisionEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Hiring Decision Question E-mailed to " + client.getName(), userId));
	}
	
}
