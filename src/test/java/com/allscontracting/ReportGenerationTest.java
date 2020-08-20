package com.allscontracting;

import static org.junit.Assert.*;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.model.Client;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ProposalRepository;
import com.allscontracting.service.LeadService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportGenerationTest {
	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";

	@Autowired LeadService leadService;
	@Autowired PasswordEncoder encoder;
	@Autowired ProposalRepository proposalRepo;
	@Autowired DataSource dataSource;
	@Autowired HttpServletResponse response;
	@Autowired ProposalRepository proposalRepository;

	@Test
	public void test_runToPdfFile() throws Exception {
		try {
			Proposal proposal = this.proposalRepo.findAll().get(2);
			Client client =  proposal.getLead().getClient();
			String sourceFile = ReportTest.class.getClassLoader().getResource(JASPER_FOLDER + "estimate.jasper").getPath().replaceFirst("/", "")  ;
			String destFile = "C:/temp/proposal" + System.currentTimeMillis() + ".pdf";
			HashMap<String, Object> map = getParams(proposal, client);
			//String res = JasperFillManager.fillReportToFile(sourceFile, map, dataSource.getConnection());
			JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void compile() throws Exception {
		try {
			String sourceFileName = ReportTest.class.getClassLoader().getResource(JASPER_FOLDER + "estimate.jrxml").getPath().replaceFirst("/", "")  ; 
			sourceFileName = JasperCompileManager.compileReportToFile(sourceFileName);
			

			Proposal proposal = this.proposalRepo.findAll().get(2);
			Client client =  proposal.getLead().getClient();
			String destFile = "C:/temp/proposal" + System.currentTimeMillis() + ".pdf";
			HashMap<String, Object> map = getParams(proposal, client);
			JasperRunManager.runReportToPdfFile(sourceFileName, destFile, map, dataSource.getConnection());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, Object> getParams(Proposal proposal, Client client) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}

}
