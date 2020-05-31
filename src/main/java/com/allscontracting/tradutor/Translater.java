package com.allscontracting.tradutor;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.model.Lead.Vendor;

public interface Translater<E extends Serializable> {
	
	boolean isFileFromRightVendor(String originalFileName, Vendor vendor);

	List<E> vendorFileToLeads(MultipartFile file)throws IOException ;

}
