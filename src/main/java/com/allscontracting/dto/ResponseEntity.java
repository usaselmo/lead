package com.allscontracting.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.Lead;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEntity {

	private final List<String> successMessages = new ArrayList<>();
	private final List<String> errorMessages = new ArrayList<>();
	private LeadDTO lead;
	private final List<LeadDTO> leads = new ArrayList<>();
	
	public ResponseEntity addSuccessMessage(String successMessage) {
		this.successMessages.add(successMessage);
		return this;
	}
	
	public ResponseEntity addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
		return this;
	}
	
	public ResponseEntity addLead(Lead lead) {
		this.leads.add(LeadDTO.leadToDTO(lead));
		return this;
	}
	
	public ResponseEntity addAllLeads(List<Lead> leads) {
		this.leads.addAll(leads.stream().map(l->LeadDTO.leadToDTO(l)).collect(Collectors.toList()));
		return this;
	}
	
}
