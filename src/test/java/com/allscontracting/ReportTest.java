package com.allscontracting;

import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import net.sf.jasperreports.engine.JasperRunManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {
	
	@Autowired DataSource dataSource;
	@Autowired PasswordEncoder encoder;
	
	
	@Test
	public void testName() throws Exception {
		String jasperFile = "D:\\java\\github\\lead\\src\\main\\resources\\jasper\\proposal2.jasper";
		String pdfDestFile = "D:/temp/proposal" + System.currentTimeMillis() + ".pdf";
		JasperRunManager.runReportToPdfFile(jasperFile, pdfDestFile, new HashMap<>(), dataSource.getConnection());
	}
	
	@Test
	public void testPassword() throws Exception {
		System.out.println(this.encoder.encode("123"));
	}

}
