package com.allscontracting.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseEntity {

	private final List<String> successMessages = new ArrayList<>();
	private final List<String> errorMessages = new ArrayList<>();
	private LeadDTO lead;
	
	
	public void addSuccessMessage(String successMessage) {
		this.successMessages.add(successMessage);
	}
	
	public void addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
	}
	
}
