package com.allscontracting.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
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

	public void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName,
			String jasperReportFileName) throws JRException, SQLException, IOException, Exception {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX);
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
		JasperRunManager.runReportToPdfStream(is, response.getOutputStream(), map, dataSource.getConnection());
	}

	private String getSourceFileName(String jasperReportFileName) throws IOException {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = this.getClass().getClassLoader().getResource(fileName).getFile(); 
		return sourceFile;  
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map,	String jasperReportFileName) throws JRException, SQLException, IOException {
		//String sourceFileName = this.getClass().getClassLoader().getResource(JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX).getFile(); 
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX);
		Path destFileName = Files.createTempFile("", fileName);
		byte[] bytes = JasperRunManager.runReportToPdf(is, map, dataSource.getConnection());
		Files.write(destFileName, bytes, StandardOpenOption.WRITE);
		//JasperRunManager.runReportToPdfFile(sourceFileName, destFileName.getAbsolutePath(), map, dataSource.getConnection());
		return destFileName.toFile();
	}

}
