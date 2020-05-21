package com.allscontracting.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.jpaimpl.LeadJpaRepository;
import com.allscontracting.service.LeadService;

@RestController
@RequestMapping("leads")
public class LeadController {

	@Autowired
	LeadService leadService;
	
	@Autowired LeadJpaRepository leadRepo;

	@GetMapping(value = "{id}/proposals")
	public List<Proposal> findProposals(@PathVariable String id) {
		try {
			List<Proposal> res = this.leadService.findLeadProposals(id);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "")
	public List<Lead> list(@RequestParam int pageRange) {
		try {
			List<Lead> res = this.leadService.listLeads(pageRange);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping(value = "/drop")
	public void drop() {
		try {
			leadService.drop();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	@GetMapping(value = "/total")
	public Long findTotal() {
		try {
			return leadService.getLeadsTotal();
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	
}
