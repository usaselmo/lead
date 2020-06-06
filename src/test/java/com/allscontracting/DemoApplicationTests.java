package com.allscontracting;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.VisitScheduledEvent;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.repo.ProposalRepository;
import com.allscontracting.service.LeadService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	LeadService leadService;
	@Autowired
	EventManager eventManager;

	@Test
	@Ignore
	public void contextLoads() {
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
		Client client;
		this.leadService.scheduleAVisit("", new Date());
	}

	@Test
	@Rollback
	@Ignore
	public void testEvent() throws Exception {
		this.eventManager.notifyAllListeners(
				new VisitScheduledEvent(Lead.builder().id("sdfsd").build(), Client.builder().id(125L).build(), new Date()));
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
	public void testReport() throws Exception {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Client client = proposal.getLead().getClient();
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal" + System.currentTimeMillis() + ".pdf";
		HashMap<String, Object> map = getParams(proposal, client);
		JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
	}


	@Test
	public void testProposalRtf() throws Exception {
		Proposal proposal = this.proposalRepo.findAll().get(2);
		Client client = proposal.getLead().getClient();
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal" + System.currentTimeMillis() + ".rtf";
		HashMap<String, Object> map = getParams(proposal, client);
		
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
		String fileName = JASPER_FOLDER + "proposal2" + JASPER_SUFFIX;
		String sourceFile = ReportTest.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		return sourceFile;
	}

	private HashMap<String, Object> getParams(Proposal proposal, Client client) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		map.put("PROPOSAL_ID", proposal.getId());
		return map;
	}

}
