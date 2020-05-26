package com.allscontracting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.ProposalRepository;

@Service
public class ProposalService {

	@Autowired ProposalRepository proposalRepository;
	@Autowired LeadRepository leadRepository;
	public Proposal save(Proposal proposal, String leadId) {
		Lead lead = this.leadRepository.findOne(leadId);
		long number = lead.getProposals().size();
		proposal.setLead(lead);
		proposal.setNumber(number+1);
		return this.proposalRepository.save(proposal);
	}

}
