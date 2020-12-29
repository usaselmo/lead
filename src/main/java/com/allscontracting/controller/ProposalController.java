package com.allscontracting.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.MailDTO;
import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.ProposalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("proposals")
@RequiredArgsConstructor
public class ProposalController extends EmailSenderAbstractController{

	private static final String UNEXPECTED_ERROR = "Unexpected error.";
	private final ProposalService proposalService;

	@GetMapping(value = "{proposalId}/markasemailed")
	public LeadEntity markAsEmailed(@PathVariable long proposalId, @Autowired Authentication authentication) {
		try {
			ProposalDTO proposalDTO = this.proposalService.markAsEmailed(proposalId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().proposal(proposalDTO).build().addSuccessMessage("Email is being sent.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not send e-mail");
		}
	}

	@PutMapping(value = "{proposalId}/email/{personId}/{leadId}")
	public void sendProposalByEmail(@PathVariable long proposalId, @RequestBody MailDTO mailDTO, @PathVariable String personId,  @PathVariable String leadId, @Autowired Authentication authentication) throws Exception {
		this.proposalService.sendPdfByEmail(proposalId, ((LeadUserDetails) authentication.getPrincipal()).getUser(), mailDTO, generateAttachments(personId+leadId));
		removeAttachments(personId+leadId); 
	}

	@PutMapping
	public LeadEntity update(@RequestBody ProposalDTO proposalDTO, @RequestParam String leadId, @Autowired Authentication authentication) {
		try {
			proposalDTO = proposalService.update(proposalDTO, leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().proposal(proposalDTO).build().addSuccessMessage("Proposal Created.");
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXPECTED_ERROR);
		}
	}

	@PostMapping(value = "")
	public LeadEntity saveProposal(@RequestBody ProposalDTO proposalDTO, @RequestParam String leadId, @Autowired Authentication authentication) {
		try {
			proposalDTO = proposalService.save(proposalDTO, leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().proposal(proposalDTO).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXPECTED_ERROR);
		}
	}

	@DeleteMapping
	public LeadEntity deleteProposal(@RequestParam String leadId, @RequestParam String proposalId, @Autowired Authentication authentication) {
		try {
			proposalService.delete(leadId, proposalId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
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

	@GetMapping(value = "/medias/{mediaId}/pdf")
	public void getMediapdf(HttpServletResponse response, @PathVariable Long mediaId) throws Exception {
		this.proposalService.getMediaAsPdfStream(mediaId, response);
	}

}
