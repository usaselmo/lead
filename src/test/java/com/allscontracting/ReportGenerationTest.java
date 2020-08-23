package com.allscontracting;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.dto.ProposalDTO;
import com.allscontracting.model.Person;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ProposalRepository;
import com.allscontracting.service.LeadService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
	public void runToFileFromStream() throws Exception {
		try {
			InputStream is = this.getJrxmlFileAsStream();
			JasperReport compiledJasperReport = JasperCompileManager.compileReport(is);

			HashMap<String, Object> map = this.definaParams();
			String destFile = this.defineDestinationFile();
			
			byte[] res = JasperRunManager.runReportToPdf(compiledJasperReport, map, dataSource.getConnection());
			Files.write(Paths.get(destFile),res);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}

	@Test
	@Ignore
	public void test_runToPdfFile() throws Exception {
		try {
			HashMap<String, Object> map = definaParams();
			InputStream is = getJrxmlFileAsStream() ;
			String destFile = defineDestinationFile();
			
			JasperFillManager.fillReport(is, map, dataSource.getConnection());
			
			//JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String defineDestinationFile() {
		return "C:/temp/proposal" + System.currentTimeMillis() + ".pdf";
	}

	private String defineSourceFile() {
		return ReportTest.class.getClassLoader().getResource(JASPER_FOLDER + "estimate.jasper").getPath().replaceFirst("/", "");
	}

	private InputStream getJrxmlFileAsStream() {
		return getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + "estimate.jrxml");
	}

	private HashMap<String, Object> definaParams() {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Person person =  proposal.getLead().getClient();
		HashMap<String, Object> map = getParams(proposal, person);
		return map;
	}
	
	@Test
	@Ignore
	public void compile() throws Exception {
		try {

			String sourceFileName = ReportTest.class.getClassLoader().getResource(JASPER_FOLDER + "estimate.jrxml").getPath().replaceFirst("/", "")  ; 
			sourceFileName = JasperCompileManager.compileReportToFile(sourceFileName);
			//sourceFileName = ReportTest.class.getClassLoader().getResource(JASPER_FOLDER + "proposal.jasper").getPath().replaceFirst("/", "")  ; 

			Proposal proposal = this.proposalRepo.findAll().get(2);
			Person person =  proposal.getLead().getClient();
			String destFile = "C:/temp/proposal.pdf";
			
			HashMap<String, Object> map = getParams(proposal, person);
			JasperRunManager.runReportToPdfFile(sourceFileName, destFile, map, dataSource.getConnection());
			
		} catch (Exception e) {
			e.printStackTrace(); 
			fail();
		}
	}

	private HashMap<String, Object> getParams(Proposal proposal, Person person) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", person);
		map.put("PROPOSAL", ProposalDTO.of(proposal));
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}

}
