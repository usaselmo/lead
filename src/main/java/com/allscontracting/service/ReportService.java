package com.allscontracting.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

	public void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName, String jasperReportFileName) throws JRException, SQLException, IOException, Exception {
		try {
			
			InputStream out = getJasperFileAsStream(map, jasperReportFileName);
			
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
      int c;
      while ((c = out.read()) != -1) {
          response.getOutputStream().write(c);
      }
      response.getOutputStream().flush();
      response.getOutputStream().close();
      //Files.delete(Paths.get(tempFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private InputStream getJasperFileAsStream(HashMap<String, Object> map, String jasperReportFileName) throws JRException, IOException, SQLException {
		String tempFile = getCompiledJasperFile(map, jasperReportFileName);
		InputStream out = Files.newInputStream(Paths.get(tempFile));
		return out;
	}

	private String getCompiledJasperFile(HashMap<String, Object> map, String jasperReportFileName) throws JRException, IOException, SQLException {
		String sourceFileName = ReportService.class.getClassLoader().getResource(JASPER_FOLDER + jasperReportFileName + JRXML_SUFFIX).getPath().replaceFirst("/", "")  ; 
		sourceFileName = JasperCompileManager.compileReportToFile(sourceFileName);
		String tempFile = Files.createTempFile("leadsdc", "").getFileName().toFile().getPath();
		JasperRunManager.runReportToPdfFile(sourceFileName, tempFile, map, dataSource.getConnection());
		return tempFile;
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map,	String jasperReportFileName) throws JRException, SQLException, IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX);
		Path destFile = Files.createTempFile("", fileName);
		byte[] bytes = JasperRunManager.runReportToPdf(is, map, dataSource.getConnection());
		Files.write(destFile, bytes, StandardOpenOption.WRITE);
		return destFile.toFile();
	}

	private String getSourceFileName(String jasperReportFileName) throws IOException {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = this.getClass().getClassLoader().getResource(fileName).getFile(); 
		return sourceFile;  
	}

}
