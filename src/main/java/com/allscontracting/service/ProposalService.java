package com.allscontracting.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	private static final String TMP_PDF = "tmp.pdf";
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
	
	public void getProposalAsPdfStream(HttpServletResponse response, String proposalId) throws Exception {
		Proposal proposal = this.proposalRepository.findOne(Long.valueOf(proposalId ));
		Client client = proposal.getLead().getClient();
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		String streamFileName = new StringBuilder(client.getName()).append(" - ").append(client.getAddress()).append(" - ").append("proposal #").append(proposal.getNumber()).append(".pdf").toString();
		getReportAsPdfStream(response, map, streamFileName, "proposal2"); 		
	}

	private void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName,
			String jasperReportFileName) throws JRException, SQLException, IOException, Exception {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		JasperRunManager.runReportToPdfFile(sourceFile, TMP_PDF, map, dataSource.getConnection());
		byte[] byteArray = Files.readAllBytes(Paths.get(TMP_PDF));
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\""+streamFileName+"\"");
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
