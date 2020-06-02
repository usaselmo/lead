package com.allscontracting.tradutor.impl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.allscontracting.model.Lead.Vendor;

@Component
@Qualifier("emailLeadTranslatorImpl")
public class EmailLeadTranslatorImpl extends NetworxLeadTranslaterImpl{

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("email") && vendor.equals(Vendor.EMAIL);
	}

	public Vendor getVendor() {
		return Vendor.EMAIL;
	}

	public static String defineId(String email, String phoneNumber) {
		return 
		Base64.getEncoder().encodeToString((email+phoneNumber).getBytes());
	}
	
}
