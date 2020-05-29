package com.allscontracting;

import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

public class ReportTest {

	public static void main(String[] args) throws IOException, DocumentException, JRException {
		String jasperFile = "D:\\java\\github\\lead\\src\\main\\resources\\jasper\\SSM8135.jasper";
		String pdfDestFile = "D:/temp/proposal" + System.currentTimeMillis() + ".pdf";
		JasperRunManager.runReportToPdfFile(jasperFile, pdfDestFile, new HashMap<>());
	}

}
