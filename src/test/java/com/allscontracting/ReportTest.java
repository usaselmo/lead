package com.allscontracting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

import com.allscontracting.model.Client;
import com.allscontracting.model.Proposal;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

public class ReportTest {

	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";

	public static void main(String[] args) throws IOException, JRException {
		
		String fileName = JASPER_FOLDER + "proposal" + JASPER_SUFFIX;
		String sourceFile = ReportTest.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		String destFile = "D:/temp/proposal.pdf";

		InputStream fis = Files.newInputStream(Paths.get(sourceFile), StandardOpenOption.READ);
		//OutputStream fos = Files.newOutputStream(Paths.get(pdfDestination), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

		//JasperRunManager.runReportToPdfStream(fis, fos, null);
		
		Client client = Client.builder().name("Mariah Carie").build();
		Proposal proposal = Proposal.builder().number(23L).build();
		HashMap<String, Object> map = new HashMap<>();
		map.put("CLIENT", client);
		map.put("PROPOSAL", proposal);
		
		JasperRunManager.runReportToPdfFile(sourceFile, destFile, map);

	}

}
