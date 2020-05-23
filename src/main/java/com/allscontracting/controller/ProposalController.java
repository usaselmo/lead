package com.allscontracting.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Proposal;
import com.allscontracting.service.FileService;

@RestController
@RequestMapping("proposals")
public class ProposalController {
	
	@Autowired private FileService fileService;

	@PostMapping(value = "{proposalId}/lead/{leadId}")
	public void sendByEmail(@RequestBody Proposal proposal, @PathVariable long proposalId, @PathVariable String leadId)
			throws IOException {
		this.fileService.sendByEmail(proposal, leadId);
	}


}
