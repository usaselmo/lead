package com.allscontracting.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    final JasperPrint jasperPrint = JasperFillManager.fillReport(getSourceFileName(jasperReportFileName), map, dataSource.getConnection());
    final JRRtfExporter saida = new JRRtfExporter();
    saida.setExporterInput(new SimpleExporterInput(jasperPrint));
    saida.setExporterOutput(new SimpleWriterExporterOutput(baos,"UTF-8"));
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
		
		String sourceFileName = getSourceFileName(jasperReportFileName);
		String tempFileName = Files.createTempFile("", "").toFile().getAbsolutePath();

		JasperRunManager.runReportToPdfFile(sourceFileName, tempFileName, map, dataSource.getConnection());
		byte[] byteArray = Files.readAllBytes(Paths.get(tempFileName));
		
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
		response.setContentLength(byteArray.length);
		ServletOutputStream os = response.getOutputStream();
		try {
			os.write(byteArray, 0, byteArray.length);
		} catch (Exception excp) {
			throw excp;
		} finally {
			os.close();
		}
	}

	private String getSourceFileName(String jasperReportFileName) {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		return sourceFile;
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map,	String jasperReportFileName) throws JRException, SQLException, IOException {
		String jasperfileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(jasperfileName).getPath().replaceFirst("/", "");
		File tmpFile = Files.createTempFile("", fileName).toFile();
		JasperRunManager.runReportToPdfFile(sourceFile, tmpFile.getAbsolutePath(), map, dataSource.getConnection());
		return tmpFile;
	}

}
