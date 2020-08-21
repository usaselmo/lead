package com.allscontracting;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Person;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ProposalRepository;
import com.allscontracting.service.LeadService;
import com.allscontracting.service.Mail;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired LeadService leadService;
	@Autowired PasswordEncoder encoder;

	@Test
	@Ignore
	public void contextLoads() {
	}
	
	@Test
	public void testPassword() throws Exception {
		System.out.println(this.encoder.encode("123"));
	}

	@Test
	@Ignore
	public void init() throws Exception {
		assertNotNull(this.leadService);
	}

	@Test
	@Ignore
	public void changeLeadEvent() throws Exception {
		LocalDateTime dateTime;
		Lead lead;
		Person person;
		//this.leadService.scheduleAVisit("", new Date());
	}

	@Test
	@Rollback
	@Ignore
	public void testEvent() throws Exception {
		//this.eventManager.notifyAllListeners(new VisitScheduledEvent(Lead.builder().id("sdfsd").build(), Person.builder().id(125L).build(), new Date()));
	}

	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";

	@Autowired
	ProposalRepository proposalRepo;
	@Autowired
	DataSource dataSource;
	@Autowired
	HttpServletResponse response;
	@Autowired
	ProposalRepository proposalRepository;

	@Test
	public void test_runToHtmlFile() throws Exception {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Person person = proposal.getLead().getPerson();
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal" + System.currentTimeMillis() + ".html";
		HashMap<String, Object> map = getParams(proposal, person);
		JasperRunManager.runReportToHtmlFile(sourceFile, destFile, map, dataSource.getConnection());
	}
	
	@Test
	public void testHtmlProposal()  {
		try {
			Proposal proposal = this.proposalRepo.findAll().get(2);
			Person person = proposal.getLead().getPerson();
			String sourceFile = getSourceFile();
			String htmlContent = "D:/temp/proposal" + System.currentTimeMillis() + ".html";
			HashMap<String, Object> map = getParams(proposal, person);
			JasperRunManager.runReportToHtmlFile(sourceFile, htmlContent, map, dataSource.getConnection());
			new Mail("anselmo.sr@gmail.com", "HTML test", new String(Files.readAllBytes(Paths.get(htmlContent)))).onError( System.out::println ).send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_runToPdfFile() throws Exception {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Person person =  proposal.getLead().getPerson();
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal" + System.currentTimeMillis() + ".pdf";
		HashMap<String, Object> map = getParams(proposal, person);
		JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
	}

	@Test
	public void testProposalRtf() throws Exception {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Person person =  proposal.getLead().getPerson();
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal" + System.currentTimeMillis() + ".rtf";
		HashMap<String, Object> map = getParams(proposal, person);
		
    //final InputStream jrFile = Files.newInputStream(Paths.get(sourceFile)/*, StandardOpenOption.READ*/); 
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFile, map, dataSource.getConnection());
    final JRRtfExporter saida = new JRRtfExporter();
    saida.setExporterInput(new SimpleExporterInput(jasperPrint));
    saida.setExporterOutput(new SimpleWriterExporterOutput(baos,"UTF-8"));
    saida.exportReport();
    byte[] bytes = baos.toByteArray();
    
    Files.write(Paths.get(destFile), bytes);

	}


	
	
	private String getSourceFile() {
		//String fileName = JASPER_FOLDER + "proposal2" + JASPER_SUFFIX;
		//String sourceFile = ReportTest.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		//return sourceFile;
		return "";
	}

	private HashMap<String, Object> getParams(Proposal proposal, Person person) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", person);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}

}
