package com.allscontracting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Client;
import com.allscontracting.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired ClientService clientService;
	
	@GetMapping("")
	public List<Client> searchClient(@RequestParam String name) {
		List<Client> res = this.clientService.findByName(name);
		return res;
	}
	
	@PutMapping("")
	public Client updateClient(@RequestBody Client client) {
		return this.clientService.updateClient(client);
	}
	
}