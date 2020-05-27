package com.allscontracting.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.controller.ProposalController;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ItemRepository;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.LineRepository;
import com.allscontracting.repo.ProposalRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

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

	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws IOException, JRException {

		String fileName = "jasper/proposal.jasper";
		String jasperFileSource = ProposalController.class.getClassLoader().getResource(fileName).getPath()
				.replaceFirst("/", "");
		
		Proposal proposal = this.proposalRepository.findOne(Long.valueOf(proposalId));
		Client client = proposal.getLead().getClient();
		
		String pdfFileName = new StringBuilder(client.getName()).append(" - ").append(client.getAddress()).append(" - ").append("proposal #").append(proposal.getNumber()).append(".pdf").toString();

		InputStream fis = Files.newInputStream(Paths.get(jasperFileSource), StandardOpenOption.READ);
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\""+pdfFileName+"\"");
		ServletOutputStream os = response.getOutputStream();
		
		JasperRunManager.runReportToPdfStream(fis, os, null);		 		
	}

}
