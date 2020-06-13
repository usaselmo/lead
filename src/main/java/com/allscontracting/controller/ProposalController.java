package com.allscontracting.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.ResponseEntity;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.ProposalService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("proposals")
public class ProposalController {
	
	@Autowired private ProposalService proposalService;

	@GetMapping(value = "{proposalId}/email")
	public void sendByEmail(@PathVariable long proposalId)
			throws IOException, JRException, SQLException {
		this.proposalService.sendPdfByEmail(proposalId);
	}

	@PostMapping(value = "")
	public Proposal saveProposal(@RequestBody Proposal proposal, @RequestParam String leadId)
			throws IOException {
		proposal =  proposalService.save(proposal, leadId);
		return proposal;
	}

	@DeleteMapping(value = "")
	public ResponseEntity deleteProposal(@RequestParam String leadId, @RequestParam String proposalId){
		try {
			proposalService.delete(leadId, proposalId);
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
