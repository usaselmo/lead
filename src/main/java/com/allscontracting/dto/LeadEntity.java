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
	private List<LeadDTO> leads;
	private Long leadsTotalPrice;
	private List<EventTypeDTO> eventTypes;
	private List<EventTypeDTO> nextEvents;
	private List<String> leadTypes;
	private List<EventLogDTO> eventLogs;
	
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
		if(this.leads==null)
			this.leads = new ArrayList<>();
		this.leads.addAll(leads.stream().map(l->LeadDTO.of(l)).collect(Collectors.toList()));
		return this;
	}
		
}
