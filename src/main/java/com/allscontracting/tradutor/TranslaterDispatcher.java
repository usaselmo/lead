package com.allscontracting.tradutor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.tradutor.impl.HomeAdvisorLeadTranslaterImpl;
import com.allscontracting.tradutor.impl.NetworxLeadTranslaterImpl;

@Component
public class TranslaterDispatcher {
	
	private Map<Vendor, Translater<?>> strategies = new HashMap<>();
	@Autowired HomeAdvisorLeadTranslaterImpl ha;
	@Autowired NetworxLeadTranslaterImpl nx;
	
	@PostConstruct
	void init() {
		this.strategies.put(Vendor.HOME_ADVISOR, this.ha);
		this.strategies.put(Vendor.NETWORX, this.nx);
	}
	
	public Translater<?> dispatch(Vendor vendor) {
		return this.strategies.get(vendor);
	}

}
