package com.allscontracting.tradutor.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.EventType;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.service.Converter;
import com.allscontracting.tradutor.Translater;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier("networxLeadTranslaterImpl")
public class NetworxLeadTranslaterImpl implements Translater<Lead>{
		
	private static final String LINE_SEPARATOR = "\n";
	public static final String ITEM_SEPARATOR = ",";
	
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
	public List<Lead> vendorFileToLeads(MultipartFile file) throws IOException {
		final String originalStr = replaceSpecialChars(new String(file.getBytes()));
		final List<String> lines = Arrays.asList(originalStr.split(LINE_SEPARATOR));
		List<Lead> leads = lines.stream().map(line -> this.replaceSpecialChars(line))
				.map(line -> importedFileLineToEntity(line, Lead.class))
				.filter(lead -> !StringUtils.isEmpty(lead.getId()))
				.collect(Collectors.toList());
		return leads;
	}

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("networx") && vendor.equals(Vendor.NETWORX);
	}

	private Vendor getVendor() {
		return Vendor.NETWORX;
	}

	private Lead importedFileLineToEntity(String line, Class<Lead> clazz) {
		if(line.contains("Subscription;Date;Name"))
			return Lead.builder().build();
		String[] splitedLine = line.split(ITEM_SEPARATOR);
		IntStream.range(0, splitedLine.length)
			.forEach(index->{
				splitedLine[index] = splitedLine[index].replace("\"=\"\"", "").replace("\"\"\"", "");
			});
		return buildLead(splitedLine); 
	}

	private String replaceSpecialChars(String line) {
		boolean aberto = false;
		char[] arr = line.toCharArray();
		for (int index = 0; index < arr.length; index++) {
			if (Character.compare('"', arr[index]) == 0) {
				aberto = !aberto;
				continue;
			}
			if (Character.compare('\n', arr[index]) == 0 && aberto) {
				arr[index] = ' ';
			}
	
		}
		return new String(arr);
	}

	private Lead buildLead(String[] splitedLine) {
		try {
			Lead lead = Lead.builder()
					.id(defineId(splitedLine))
					.vendor(getVendor())
					.date(Converter.convertToDate(splitedLine[NX_Date]))
					.description(splitedLine[NX_Additional_Information])
					.fee(LeadHelper.defineCost(splitedLine[NX_Cost]))
					.type(splitedLine[NX_Task])
					.event(EventType.BEGIN)
					.client(Client.builder()
							.address(splitedLine[NX_Address] + ", " + splitedLine[NX_City] + ", " + splitedLine[NX_State] + " " + splitedLine[NX_Zip_Code])
							.phone(splitedLine[NX_Phone_Number].replaceAll("(|)|-", ""))
							.cellPhone("")
							.email(splitedLine[NX_Email])
							.name(splitedLine[NX_Name]+"")
							.build())
					.build();
			log.info("Lead converted successfully, {}", lead);
			return lead;
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
