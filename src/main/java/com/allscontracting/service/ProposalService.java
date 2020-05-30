package com.allscontracting.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.ReportTest;
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

	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";
	@Autowired DataSource dataSource;
	
	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws IOException, JRException, SQLException {
		String fileName = JASPER_FOLDER + "proposal2" + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		Proposal proposal = this.proposalRepository.findOne(Long.valueOf(proposalId ));
		Client client = proposal.getLead().getClient();
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		String destFile = "D:/temp/proposal.pdf";		 
		JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
		byte[] byteArray = Files.readAllBytes(Paths.get(destFile));
		String pdfFileName = new StringBuilder(client.getName()).append(" - ").append(client.getAddress()).append(" - ").append("proposal #").append(proposal.getNumber()).append(".pdf").toString();
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\""+pdfFileName+"\"");
		ServletOutputStream os = response.getOutputStream();
		try {
		   os.write(byteArray , 0, byteArray.length);
		} catch (Exception excp) {
		   throw excp;
		} finally {
		    os.close();
		} 		
	}

}
