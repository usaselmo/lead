package com.allscontracting.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.fsimpl.LeadRepository;
import com.allscontracting.repo.jpaimpl.LeadJpaRepository;
import com.allscontracting.service.MailService;

@RestController
@RequestMapping("proposals")
public class ProposalController {

	@Autowired	MailService mailService;
	@Autowired	LeadJpaRepository leadRepository;

	@PostMapping(value = "{proposalId}/lead/{leadId}")
	public void sendByEmail(@RequestBody Proposal proposal, @PathVariable long proposalId, @PathVariable String leadId)
			throws IOException {
		Lead lead = leadRepository.findOne(leadId);
		File proposalPdfFile = new File(LeadRepository.PROPOSALS_FOLDER + "\\" + proposal.getFileName());
		Client client = lead.getClient();
		this.mailService.sendProposalByEmail(proposal, client, proposalPdfFile);
	}
}
