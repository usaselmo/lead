package com.allscontracting.dto;

import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.Lead;
import com.allscontracting.service.Converter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeadDTO {
	private String id;
	private String vendor;
	private String date;
	private String description;
	private String fee;
	private String type; 
	private String notes;
	private ClientDTO client;
	private List<ProposalDTO> proposals;
	private String event;//EventType
	private String visit;
	
	public static final LeadDTO leadToDTO(Lead lead) {
		return LeadDTO.builder()
				.id(lead.getId())
				.vendor(lead.getVendor().toString())
				.date(Converter.dateToString(lead.getDate()))
				.description(lead.getDescription())
				.fee(NumberFormat.getCurrencyInstance().format(lead.getFee()))
				.type(lead.getType())
				.notes(lead.getNotes())
				.client(ClientDTO.clientToDTO(lead.getClient()))
				.proposals(lead.getProposals().stream().map(p->ProposalDTO.toDTO(p)).collect(Collectors.toList()))
				.event(lead.getEvent().toString())
				.visit(lead.getVisit()!=null?Converter.dateToString(lead.getVisit()):"")
				.build();
	}
}
