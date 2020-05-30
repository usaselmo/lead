package com.allscontracting;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

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

import net.sf.jasperreports.engine.JasperRunManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired LeadService leadService;
	@Autowired EventManager eventManager;

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
		this.eventManager.notifyAllListeners(new VisitScheduledEvent(Lead.builder().id("sdfsd").build(), Client.builder().id(125L).build(), new Date()));
	}

	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";

	@Autowired ProposalRepository proposalRepo;
	@Autowired DataSource dataSource;	
	@Autowired HttpServletResponse response;
	@Autowired ProposalRepository proposalRepository;

	@Test
	public void testReport() throws Exception {
		
		Proposal proposal = this.proposalRepo.findAll().get(0);
		Client client = proposal.getLead().getClient();
		
		String sourceFile = getSourceFile();
		String destFile = "D:/temp/proposal.pdf";
		
		HashMap<String, Object> map = getParams(proposal, client);
		
		JasperRunManager.runReportToPdfFile(sourceFile, destFile, map, dataSource.getConnection());
	}

	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
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
