package com.allscontracting.service;

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
import net.sf.jasperreports.engine.JasperRunManager;

@Service
public class ReportService {

	@Autowired
	DataSource dataSource;
	//private static final String TMP_PDF = "tmp.pdf";
	private static final String JASPER_FOLDER = "jasper/";
	private static final String JASPER_SUFFIX = ".jasper";

	public void getReportAsPdfStream(HttpServletResponse response, HashMap<String, Object> map, String streamFileName,
			String jasperReportFileName) throws JRException, SQLException, IOException, Exception {
		String fileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(fileName).getPath().replaceFirst("/", "");
		final String tmp = Files.createTempFile("", "").toFile().getAbsolutePath();
		JasperRunManager.runReportToPdfFile(sourceFile, tmp, map, dataSource.getConnection());
		byte[] byteArray = Files.readAllBytes(Paths.get(tmp));
		response.setContentType("application/pdf");
		response.setHeader("content-disposition", "attachment; filename=\"" + streamFileName + "\"");
		ServletOutputStream os = response.getOutputStream();
		try {
			os.write(byteArray, 0, byteArray.length);
		} catch (Exception excp) {
			throw excp;
		} finally {
			os.close();
		}
	}

	public File getReportAsPdfFile(String fileName, HashMap<String, Object> map,	String jasperReportFileName) throws JRException, SQLException, IOException {
		String jasperfileName = JASPER_FOLDER + jasperReportFileName + JASPER_SUFFIX;
		String sourceFile = ProposalService.class.getClassLoader().getResource(jasperfileName).getPath().replaceFirst("/", "");
		File tmpFile = Files.createTempFile("", fileName).toFile();
		JasperRunManager.runReportToPdfFile(sourceFile, tmpFile.getAbsolutePath(), map, dataSource.getConnection());
		return tmpFile;
	}

}
