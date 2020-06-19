package com.allscontracting.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.ResponseEntity;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Proposal;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.ProposalService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("proposals")
public class ProposalController {
	
	@Autowired private ProposalService proposalService;

	@GetMapping(value = "{proposalId}/email")
	public void sendByEmail(@PathVariable long proposalId, @Autowired Authentication authentication)
			throws IOException, JRException, SQLException, LeadsException {
		this.proposalService.sendPdfByEmail(proposalId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
	}

	@PostMapping(value = "")
	public Proposal saveProposal(@RequestBody Proposal proposal, @RequestParam String leadId, @Autowired Authentication authentication)
			throws IOException, LeadsException {
		proposal =  proposalService.save(proposal, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
		return proposal;
	}

	@DeleteMapping(value = "")
	public ResponseEntity deleteProposal(@RequestParam String leadId, @RequestParam String proposalId, @Autowired Authentication authentication){
		try {
			proposalService.delete(leadId, proposalId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
			return new ResponseEntity().addSuccessMessage("Proposal Deleted");
		} catch (Exception e) {
			return new ResponseEntity().addErrorMessage(e.getMessage());
		}
	}

	@GetMapping(value = "{proposalId}/pdf")
	public void getProposalpdf(HttpServletResponse response, @PathVariable String proposalId) throws Exception {
		this.proposalService.getProposalAsPdfStream(response, proposalId);
	}

	@GetMapping(value = "{proposalId}/rtf")
	public void getProposalRtf(HttpServletResponse response, @PathVariable String proposalId) throws Exception {
		this.proposalService.getProposalAsRtfStream(response, proposalId);
	}

}
