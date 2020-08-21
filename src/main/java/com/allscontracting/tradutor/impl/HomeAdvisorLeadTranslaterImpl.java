package com.allscontracting.tradutor.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Person;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.service.Converter;
import com.allscontracting.tradutor.Translater;

@Component
public class HomeAdvisorLeadTranslaterImpl implements Translater<Lead> {

	public static final String LINE_ITEM_SEPARATOR = ";";
	public static final String COMMA = ";";


	@Override
	public List<Lead> vendorFileToLeads(MultipartFile file) throws IOException, LeadsException {
		ArrayList<Lead> leads = new ArrayList<>();
		Document doc = Jsoup.parse(new String(file.getBytes()));
		for (Element tbody : doc.select("tbody")) {
			for (Element tr : tbody.select("tr")) {
				Elements tds = tr.select("td");
				for (Iterator<Element> iterator = tds.iterator(); iterator.hasNext();) {
				  buildLead(leads, iterator);
				}
			}
		}
		return leads;
	}

	private void buildLead(ArrayList<Lead> leads, Iterator<Element> iterator) throws LeadsException {
		leads.add(Lead.builder()
			.oldid(iterator.next().html())
			.vendor(Vendor.HOME_ADVISOR)
			.date(Converter.convertToDate(iterator.next().html()))
			.description(removeSymbol(iterator.next().html()))
			.fee(LeadHelper.defineCost(iterator.next().html()))
			.type(removeSymbol(iterator.next().html()))
			.event(Event.BEGIN)
			.client(Person.builder()
					.name(StringUtils.capitalize(removeSymbol(iterator.next().html())) + " " + StringUtils.capitalize(removeSymbol(iterator.next().html())))
					.address(removeSymbol(iterator.next().html()) + ", " + removeSymbol(iterator.next().html()) + ", " + removeSymbol(iterator.next().html()) + " " + removeSymbol(iterator.next().html()))
					.cellPhone(iterator.next().html().replaceAll("\\(|\\)|\\-| ", ""))
					.phone(removeSymbol(iterator.next().html()).replaceAll("\\(|\\)|\\-| ", ""))
					.email(removeSymbol(iterator.next().html()))
					.build())
			.build());
	}

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("home") && originalFileName.toLowerCase().contains("advisor")
				&& vendor.equals(Vendor.HOME_ADVISOR);
	}


	
}
