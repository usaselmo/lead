package com.allscontracting.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.model.Client;
import com.allscontracting.repo.ClientRepository;

@Service
public class ClientService {

	@Autowired ClientRepository clientRepo;
	
	@Transactional
	public Client updateClient(Client client) {
		Client localClient = this.clientRepo.findOne(client.getId());
		localClient.setAddress(client.getAddress());
		localClient.setEmail(client.getEmail());
		localClient.setName(client.getName());
		localClient.setPhone(client.getPhone());
		return this.clientRepo.save(localClient);
	}

	public List<Client> findByName(String name) {
		return this.clientRepo.findLikeName(name);
	}

	
}
