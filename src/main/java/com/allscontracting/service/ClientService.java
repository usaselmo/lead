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
import com.allscontracting.repo.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService {

	@Autowired ClientRepository clientRepo;
	@Autowired MailService mailService;
	@Autowired EventManager eventManager;
	
	@Transactional
	public ClientDTO updateClient(ClientDTO clientDTO, Long userId) throws NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(clientDTO.getId())).orElseThrow(()->new LeadsException("Client not found"));
		client.setAddress(clientDTO.getAddress());
		client.setEmail(clientDTO.getEmail());
		client.setName(clientDTO.getName());
		client.setPhone(clientDTO.getPhone());
		this.eventManager.notifyAllListeners(
				new AuditEvent(Client.class.getSimpleName(), String.valueOf(client.getId()), "Client updated: " + client.toString(), userId)
				);
		return ClientDTO.clientToDTO(clientRepo.save(client));
	}

	public List<ClientDTO> findByName(String name) {
		return this.clientRepo.findLikeName(name).stream().map(c->ClientDTO.clientToDTO(c)).collect(Collectors.toList());
	}

	public void sendCantReachEmail(String id, String leadId, Long userId) throws IOException, NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Client not found"));
		this.mailService.sendCantReachEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Can't Reach E-mail sent to " + client.getName(), userId));
	}

	public void sendHiringDecisionEmail(String id, String leadId, Long userId) throws IOException, NumberFormatException, LeadsException {
		Client client = this.clientRepo.findById(Long.valueOf(id)).orElseThrow(()->new LeadsException("Client not found"));
		this.mailService.sendHiringDecisionEmail(client).onError( (error)->log.error("Error sending e-mail: "+error) ).send();;
		this.eventManager.notifyAllListeners(new AuditEvent(Lead.class.getSimpleName(), leadId, "Hiring Decision Question E-mailed to " + client.getName(), userId));
	}
	
}
