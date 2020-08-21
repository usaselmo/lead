package com.allscontracting.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.repo.ProposalRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@Service
public class ReportService {

	@Autowired
	DataSource dataSource;
	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";
	private static final String JRXML_SUFFIX = ".jrxml";

	public void getReportAsRtfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName,
			String jasperReportFileName) throws IOException, JRException, SQLException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final JasperPrint jasperPrint = JasperFillManager.fillReport(getSourceFileName(jasperReportFileName), map,
				dataSource.getConnection());
		final JRRtfExporter saida = new JRRtfExporter();
		saida.setExporterInput(new SimpleExporterInput(jasperPrint));
		saida.setExporterOutput(new SimpleWriterExporterOutput(baos, "UTF-8"));
		saida.exportReport();
		byte[] bytes = baos.toByteArray();
		response.setContentType("application/rtf");
		response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes, 0, bytes.length);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	@Autowired ProposalRepository proposalRepo;
	private String tempFile;

	public void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName, String jasperReportFileName) throws JRException, SQLException, IOException, Exception {
		try {
			InputStream out = getJasperFileAsStream(map, jasperReportFileName, streamFileName);
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
      int c;
      while ((c = out.read()) != -1) {
          response.getOutputStream().write(c);
      }
      response.getOutputStream().flush();
      response.getOutputStream().close();
      Files.deleteIfExists(Paths.get(this.tempFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private InputStream getJasperFileAsStream(HashMap<String, Object> map, String jasperReportFileName, String fileName) throws JRException, IOException, SQLException {
		String tempFile = getCompiledJasperFile(fileName, map, jasperReportFileName);
		InputStream out = Files.newInputStream(Paths.get(tempFile));
		return out; 
	} 

	private String getCompiledJasperFile(String fileName, HashMap<String, Object> map, String jasperReportFileName) throws JRException, IOException, SQLException {
		String sourceFileName = ReportService.class.getClassLoader().getResource(JASPER_FOLDER + jasperReportFileName + JRXML_SUFFIX).getPath().replaceFirst("/", "")  ; 
		sourceFileName = JasperCompileManager.compileReportToFile(sourceFileName);
		this.tempFile = Files.createTempFile("", fileName).getFileName().toFile().getPath();
		JasperRunManager.runReportToPdfFile(sourceFileName, tempFile, map, dataSource.getConnection());
		return tempFile;
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map,	String jasperReportFileName) throws JRException, SQLException, IOException {
		return new File(this.getCompiledJasperFile(fileName, map, jasperReportFileName));
	}

	private String getSourceFileName(String jasperReportFileName) throws IOException {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = this.getClass().getClassLoader().getResource(fileName).getFile(); 
		return sourceFile;  
	}

}
