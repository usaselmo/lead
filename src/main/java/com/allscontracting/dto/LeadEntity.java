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
public class LeadEntity {

	private final List<String> successMessages = new ArrayList<>();
	private final List<String> errorMessages = new ArrayList<>();
	private LeadDTO lead;
	private final List<LeadDTO> leads = new ArrayList<>();
	private List<EventTypeDTO> eventTypes;
	private List<String> leadTypes;
	
	public LeadEntity addSuccessMessage(String successMessage) {
		this.successMessages.add(successMessage);
		return this;
	}
	
	public LeadEntity addErrorMessage(String errorMessage) {
		this.errorMessages.add(errorMessage);
		return this;
	}
	
	public LeadEntity addLead(Lead lead) {
		this.leads.add(LeadDTO.of(lead));
		return this;
	}
	
	public LeadEntity addAllLeads(List<Lead> leads) {
		this.leads.addAll(leads.stream().map(l->LeadDTO.of(l)).collect(Collectors.toList()));
		return this;
	}
	
}
