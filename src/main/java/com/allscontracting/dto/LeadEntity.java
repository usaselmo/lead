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

	private List<String> successMessages ;
	private List<String> errorMessages ;
	private LeadDTO lead;
	private List<LeadDTO> leads;
	private Long leadsTotalPrice;
	private List<EventDTO> events;
	private List<String> leadTypes;
	private Long totalLeads;
	private CompanyDTO company;
	private ProposalDTO proposal;
	private PersonDTO person;
	private InvitationDTO invitation;
	
	public LeadEntity addSuccessMessage(String successMessage) {
		if(this.successMessages == null)
			this.successMessages = new ArrayList<>();
		this.successMessages.add(successMessage);
		return this;
	}
	
	public LeadEntity addErrorMessage(String errorMessage) {
		if(this.errorMessages == null)
			this.errorMessages = new ArrayList<>();
		this.errorMessages.add(errorMessage);
		return this;
	}
	
	public LeadEntity addLead(Lead lead) {
		if(this.leads == null)
			this.leads = new ArrayList<>();
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
