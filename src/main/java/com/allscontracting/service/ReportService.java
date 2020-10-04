package com.allscontracting.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

@Service
@AllArgsConstructor
public class ReportService {

	private DataSource dataSource;
	
	private Path tempFile;
	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";
	private static final String JRXML_SUFFIX = ".jrxml";

	public void getReportAsRtfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName, String jasperReportFileName)
			throws IOException, JRException, SQLException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final JasperPrint jasperPrint = JasperFillManager.fillReport(getSourceFileName(jasperReportFileName), map, dataSource.getConnection());
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

	public void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName, String jasperReportFileName)
			throws JRException, SQLException, IOException, Exception {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + jasperReportFileName + JRXML_SUFFIX);
			JasperReport compiledJasperReport = JasperCompileManager.compileReport(is);
			byte[] res = JasperRunManager.runReportToPdf(compiledJasperReport, map, dataSource.getConnection());
			getFileAsPdfStream(response, streamFileName, res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getFileAsPdfStream(HttpServletResponse response, String userFriendlyFileNmae, byte[] fileContentBytes) throws IOException {
		this.getFileAsStream(response, userFriendlyFileNmae, fileContentBytes, "application/pdf");
	}	
	
	public void getFileAsStream(HttpServletResponse response, String userFriendlyFileNmae, byte[] fileContentBytes, String contentType) throws IOException {
		response.setContentType(contentType);
		response.setHeader("content-disposition", "attachment; filename=\"" + userFriendlyFileNmae + "\"");
		this.tempFile = Files.createTempFile("", "");
		Path p = Files.write(this.tempFile, fileContentBytes);
		InputStream nis = Files.newInputStream(p);
		int c;
		while ((c = nis.read()) != -1) {
			response.getOutputStream().write(c);
		}
		response.getOutputStream().flush();
		response.getOutputStream().close();
		Files.deleteIfExists(this.tempFile);
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map, String jasperReportFileName) throws JRException, SQLException, IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(JASPER_FOLDER + jasperReportFileName + JRXML_SUFFIX);
		JasperReport compiledJasperReport = JasperCompileManager.compileReport(is);
		byte[] res = JasperRunManager.runReportToPdf(compiledJasperReport, map, dataSource.getConnection());
		this.tempFile = Files.createTempFile("leadsdc-", fileName.replace("/", "_"));
		return Files.write(this.tempFile, res).toFile();
	}

	private String getSourceFileName(String jasperReportFileName) throws IOException {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ReportService.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		return sourceFile;
	}

}
