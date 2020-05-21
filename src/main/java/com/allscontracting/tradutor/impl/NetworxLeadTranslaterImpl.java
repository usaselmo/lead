package com.allscontracting.tradutor.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.service.Converter;
import com.allscontracting.tradutor.Translater;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NetworxLeadTranslaterImpl implements Translater<Lead>{
		
	public static final String LINE_ITEM_SEPARATOR = ";";
	public static final String COMMA = ";";
	
	//private static final int NX_Subscription = 0;
	private static final int NX_Date = 1;
	private static final int NX_Name = 2;
	private static final int NX_Phone_Number = 3;
	private static final int NX_Email = 4;
	private static final int NX_Zip_Code = 5;
	private static final int NX_City = 6;
	private static final int NX_State = 7;
	private static final int NX_Address = 8;
	private static final int NX_Additional_Information = 9;
	private static final int NX_Task = 10;
	private static final int NX_Cost = 11;
	
	@Override
	public String entityToLocalFSFileLine(Lead entity) {
		return LeadHelper.entityToLocalFSFileLine(entity);
	}
	
	@Override
	public Lead localFSFileLineToEntity(String line, Class<Lead> clazz) {
		return LeadHelper.localFSFileLineToEntity(line, clazz);
	}

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("networx") && vendor.equals(Vendor.NETWORX);
	}

	@Override
	public Lead importedFileLineToEntity(String line, Class<Lead> clazz) {
		if(line.contains("Subscription;Date;Name"))
			return Lead.builder().build();
		String[] splitedLine = line.split(LINE_ITEM_SEPARATOR);
		IntStream.range(0, splitedLine.length)
			.forEach(index->{
				splitedLine[index] = splitedLine[index].replace("\"=\"\"", "").replace("\"\"\"", "");
			});
		return buildLead(splitedLine); 
	}

	private Lead buildLead(String[] splitedLine) {
		try {
			return Lead.builder()
					.id(defineId(splitedLine))
					.vendor(Vendor.NETWORX)
					.date(Converter.convertToDate(splitedLine[NX_Date]))
					.description(splitedLine[NX_Additional_Information])
					.fee(LeadHelper.defineCost(splitedLine[NX_Cost]))
					.type(splitedLine[NX_Task])
					.client(Client.builder()
							.address(splitedLine[NX_Address] + ", " + splitedLine[NX_City] + ", " + splitedLine[NX_State] + " " + splitedLine[NX_Zip_Code])
							.cellPhone(splitedLine[NX_Phone_Number].replaceAll("(|)|-", ""))
							.email(splitedLine[NX_Email])
							.name(splitedLine[NX_Name]+"")
							.phone("")
							.build())
					.build();
		} catch (Exception e) { 
			log.error("Line to Lead error {} {}", e.getMessage(), Arrays.asList(splitedLine));
			return Lead.builder().build();
		}
	}

	private String defineId(String[] splitedLine) {
		return 
		Base64.getEncoder().encodeToString((splitedLine[NX_Email]+splitedLine[NX_Phone_Number]).getBytes());
	}

}
