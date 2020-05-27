package com.allscontracting.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.model.Proposal;
import com.allscontracting.service.FileService;
import com.allscontracting.service.ProposalService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

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
		proposal =  proposalService.save(proposal, leadId);
		return proposal;
	}

	@DeleteMapping(value = "")
	public ResponseEntity<Object> deleteProposal(@RequestParam String leadId, @RequestParam String proposalId){
		try {
			proposalService.delete(leadId, proposalId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}


	@GetMapping(value = "{proposalId}/pdf")
	public void getProposalpdf(HttpServletResponse response, @PathVariable String proposalId)
			throws JRException, IOException {

		this.proposalService.getProposalAsPdfStream(response, proposalId);
		
		}
	
}
