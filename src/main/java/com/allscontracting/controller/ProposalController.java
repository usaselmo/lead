package com.allscontracting.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Proposal;
import com.allscontracting.service.FileService;
import com.allscontracting.service.ProposalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("proposals")
public class ProposalController {
	
	@Autowired private FileService fileService;
	@Autowired private ProposalService proposalService;

	@PostMapping(value = "{proposalId}/lead/{leadId}")
	public void sendByEmail(@RequestBody Proposal proposal, @PathVariable long proposalId, @PathVariable String leadId)
			throws IOException {
		this.fileService.sendByEmail(proposal, leadId);
	}



	@PostMapping(value = "")
	public Proposal saveProposal(@RequestBody Proposal proposal, @RequestParam String leadId)
			throws IOException {
		return this.proposalService.save(proposal, leadId);
	}


}
