package com.allscontracting.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired ClientService clientService;

	@GetMapping("{clientId}/leads/{leadId}/hiringdecision")
	public void sendHiringDecisionEmail(@PathVariable String clientId, @PathVariable String leadId, @Autowired Authentication authentication) throws IOException, NumberFormatException, LeadsException {
		clientService.sendHiringDecisionEmail(clientId, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
	}

	@GetMapping("{clientId}/leads/{leadId}/cantreach")
	public void sendCantReachEmail(@PathVariable String clientId, @PathVariable String leadId, @Autowired Authentication authentication) throws IOException, NumberFormatException, LeadsException {
		clientService.sendCantReachEmail(clientId, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
	}
	
	@GetMapping("")
	public List<Client> searchClient(@RequestParam String name) {
		List<Client> res = this.clientService.findByName(name);
		return res;
	}
	
	@PutMapping("")
	public Client updateClient(@RequestBody Client client, @Autowired Authentication authentication) throws LeadsException {
		return this.clientService.updateClient(client, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
	}
	
}
