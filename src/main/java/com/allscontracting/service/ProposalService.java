package com.allscontracting.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ItemRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.LineRepository;
import com.allscontracting.repo.ProposalRepository;

@Service
public class ProposalService {

	@Autowired ProposalRepository proposalRepository;
	@Autowired LeadRepository leadRepository;
	@Autowired ItemRepository itemRepository;
	@Autowired LineRepository lineRepository;
	
	@Transactional
	public Proposal save(Proposal proposal, String leadId) {
		Lead lead = this.leadRepository.findOne(leadId);
		proposal.setLead(lead);

		long number = lead.getProposals().size();
		proposal.setNumber(number+1);
		
		proposal.getItems().forEach(item-> {
			proposal.addItem(item);
			item.getLines().forEach(line->item.addLine(line)); 
		});

		Proposal res = this.proposalRepository.save(proposal);
		
		return res;
	}

	@Transactional
	public void delete(String leadId, String proposalId) {
		Lead lead = this.leadRepository.findOne(leadId);
		Proposal proposal = this.proposalRepository.findOne(Long.valueOf(proposalId));
		lead.removeProposal(proposal);
		this.leadRepository.save(lead);
		this.proposalRepository.delete(proposal);// TODO Auto-generated method stub
	}

}
