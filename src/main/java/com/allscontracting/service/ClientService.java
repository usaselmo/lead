package com.allscontracting.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.ClientDTO;
import com.allscontracting.event.AuditEvent;
import com.allscontracting.event.EventManager;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.User;
import com.allscontracting.repo.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService {

	@Autowired ClientRepository clientRepo;
	@Autowired MailService mailService;
	@Autowired EventManager eventManager;
	
	@Transactional
	public ClientDTO updateClient(ClientDTO clientDTO, User user) throws NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(clientDTO.getId())).orElseThrow(()->new LeadsException("Client not found"));
		client.setAddress(clientDTO.getAddress());
		client.setEmail(clientDTO.getEmail());
		client.setName(clientDTO.getName());
		client.setPhone(clientDTO.getPhone());
		this.eventManager.notifyAllListeners(
				new AuditEvent(Client.class.getSimpleName(), String.valueOf(client.getId()), "Client updated: " + client.toString(), user)
				);
		return ClientDTO.of(clientRepo.save(client));
	}

	public List<ClientDTO> findByName(String name) {
		return this.clientRepo.findLikeName(name).stream().map(c->ClientDTO.of(c)).collect(Collectors.toList());
	}

	public void sendCantReachEmail(String id, String leadId, User user) throws IOException, NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Client not found"));
		this.mailService.sendCantReachEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Can't Reach E-mail sent to " + client.getName(), user));
	}

	public void sendHiringDecisionEmail(String id, String leadId, User user) throws IOException, NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Client not found"));
		this.mailService.sendHiringDecisionEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Hiring Decision Question E-mailed to " + client.getName(), user));
	}
	
}
