package com.allscontracting.controller;

import java.io.IOException;

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

import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.ProposalService;

@RestController
@RequestMapping("proposals")
public class ProposalController {
	
	@Autowired private ProposalService proposalService;

	@GetMapping(value = "{proposalId}/email")
	public LeadEntity sendByEmail(@PathVariable long proposalId, @Autowired Authentication authentication) {
		try {
			this.proposalService.sendPdfByEmail(proposalId, ((LeadUserDetails)authentication.getPrincipal()).getUser());
			return LeadEntity.builder().build().addSuccessMessage("Email is being sent.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not send e-mail");
		}
	}

	@PostMapping(value = "")
	public ProposalDTO saveProposal(@RequestBody ProposalDTO proposalDTO, @RequestParam String leadId, @Autowired Authentication authentication)
			throws IOException, LeadsException {
		proposalDTO =  proposalService.save(proposalDTO, leadId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
		return proposalDTO;
	}

	@DeleteMapping(value = "")
	public LeadEntity deleteProposal(@RequestParam String leadId, @RequestParam String proposalId, @Autowired Authentication authentication){
		try {
			proposalService.delete(leadId, proposalId, ((LeadUserDetails)authentication.getPrincipal()).getUser().getId());
			return new LeadEntity().addSuccessMessage("Proposal Deleted");
		} catch (Exception e) {
			return new LeadEntity().addErrorMessage(e.getMessage());
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
