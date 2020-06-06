package com.allscontracting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Client;
import com.allscontracting.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired ClientService clientService;
	
	@PutMapping("")
	public Client updateClient(@RequestBody Client client) {
		return this.clientService.updateClient(client);
	}
}
