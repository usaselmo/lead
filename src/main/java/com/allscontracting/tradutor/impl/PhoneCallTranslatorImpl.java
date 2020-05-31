package com.allscontracting.tradutor.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.allscontracting.model.Lead.Vendor;

@Component
@Qualifier("phoneCallTranslatorImpl")
public class PhoneCallTranslatorImpl extends NetworxLeadTranslaterImpl {

	@Override
	public boolean isFileFromRightVendor(String originalFileName, Vendor vendor) {
		return originalFileName.toLowerCase().contains("phone") && originalFileName.toLowerCase().contains("call")
				&& vendor.equals(Vendor.PHONE_CALL);
	}

	public Vendor getVendor() {
		return Vendor.PHONE_CALL;
	}

}
