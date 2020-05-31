package com.allscontracting.tradutor.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.EventType;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.service.Converter;
import com.allscontracting.tradutor.Translater;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HomeAdvisorLeadTranslaterImpl implements Translater<Lead> {

	public static final String LINE_ITEM_SEPARATOR = ";";
	public static final String COMMA = ";";

	private static final int HA_ID = 0;
	private static final int HA_DATE = 1;
	private static final int HA_DESCRIPTION= 2;
	private static final int HA_FEE = 3;
	private static final int HA_TYPE = 4;
	private static final int HA_FIRST_NAME = 5;
	private static final int HA_LAST_NAME = 6;
	private static final int HA_ADDRESS = 7;
	private static final int HA_CITY = 8;
	private static final int HA_STATE = 9;
	private static final int HA_ZIP_CODE = 10;
	private static final int HA_CELL_PHONE = 11;
	private static final int HA_PHONE = 12;
	private static final int HA_EMAIL = 13;

	@Override
	public List<Lead> vendorFileToLeads(MultipartFile file) throws IOException, LeadsException {
			return parseFromHtmlFile(file);
			//return parseFromCsvFile(file);
	}

	private List<Lead> parseFromHtmlFile(MultipartFile file) throws IOException, LeadsException {
		ArrayList<Lead> leads = new ArrayList<>();
		Document doc = Jsoup.parse(new String(file.getBytes()));
		for (Element tbody : doc.select("tbody")) {
			for (Element tr : tbody.select("tr")) {
				Elements tds = tr.select("td");
				for (Iterator<Element> iterator = tds.iterator(); iterator.hasNext();) {
				  leads.add(Lead.builder()
						.id(iterator.next().html())
						.vendor(Vendor.HOME_ADVISOR)
						.date(Converter.convertToDate(iterator.next().html()))
						.description(iterator.next().html())
						.fee(LeadHelper.defineCost(iterator.next().html()))
						.type(iterator.next().html())
						.event(EventType.BEGIN)
						.client(Client.builder()
								.name(iterator.next().html() + " " + iterator.next().html())
								.address(iterator.next().html() + ", " + iterator.next().html() + ", " + iterator.next().html() + " " + iterator.next().html())
								.cellPhone(iterator.next().html().replaceAll("\\(|\\)|\\-| ", ""))
								.phone(iterator.next().html().replaceAll("\\(|\\)|\\-| ", ""))
								.email(iterator.next().html())
								.build())
						.build());
				}
			}
		}
		return leads;
	}

	private List<Lead> parseFromCsvFile(MultipartFile file) throws IOException {
		List<String> lines = Arrays.asList(new String(file.getBytes()).split(System.lineSeparator()));
		List<Lead> leads = lines.stream()
			.map(line->line.replaceAll("", ""))
			.map(line -> importedFileLineToEntity(line, Lead.class))
			.filter(lead -> !StringUtils.isEmpty(lead.getId()))
			.collect(Collectors.toList());
		return leads;
	}

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("home") && originalFileName.toLowerCase().contains("advisor")
				&& vendor.equals(Vendor.HOME_ADVISOR);
	}

	private Lead importedFileLineToEntity(String line, Class<Lead> clazz) {
		try {
			if(line.contains("Lead #,") && line.contains("Lead Date"))
				return Lead.builder().build();
			String[] splitedLine = line.split(LINE_ITEM_SEPARATOR);
			return Lead.builder()
					.id(splitedLine[HA_ID])
					.vendor(Vendor.HOME_ADVISOR)
					.date(Converter.convertToDate(splitedLine[HA_DATE]))
					.description(splitedLine[HA_DESCRIPTION])
					.fee(LeadHelper.defineCost(splitedLine[HA_FEE]))
					.type(splitedLine[HA_TYPE])
					.event(EventType.BEGIN)
					.client(Client.builder()
							.address(splitedLine[HA_ADDRESS] + ", " + splitedLine[HA_CITY] + ", " + splitedLine[HA_STATE] + " " + splitedLine[HA_ZIP_CODE])
							.cellPhone(splitedLine[HA_CELL_PHONE].replaceAll("\\(|\\)|\\-| ", ""))
							.phone(splitedLine[HA_PHONE].replaceAll("\\(|\\)|\\-| ", ""))
							.email(splitedLine[HA_EMAIL])
							.name(splitedLine[HA_FIRST_NAME] + " " + splitedLine[HA_LAST_NAME])
							.build())
					.build();
		} catch (Exception e) {
			log.error("Line to Lead error: {} - {}", line, e.getMessage());
			return Lead.builder().build();
		}
	}

	
}
