package com.allscontracting.tradutor;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.model.Lead.Vendor;

public interface Translater<E extends Serializable> {
	
	boolean isFileFromRightVendor(String originalFileName, Vendor vendor);

	List<E> vendorFileToLeads(MultipartFile file)throws Exception ;
	
	default String removeSymbol(String str) {
		if(str.startsWith("\""))
			str = str.replaceFirst("\"", "");
		if(str.startsWith(","))
			str = str.replaceFirst(",", "");
		if(str.endsWith("\""))
			str = str.substring(0, str.length()-1);
		return str.trim();
	}

}
