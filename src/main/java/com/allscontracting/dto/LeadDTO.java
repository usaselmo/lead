package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.comparator.Comparators;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	private String lastProposalTotal;
	
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
				.lastProposalTotal(  getLastProposalTotal(lead)  )
				.build();
	}

	private static String getLastProposalTotal(Lead lead) {
		BigDecimal total = lead.getProposals().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(Proposal.builder().total(BigDecimal.ZERO).build()).getTotal();
		if(total==null || total.equals(BigDecimal.ZERO))
			return null;
		return NumberFormat.getCurrencyInstance().format(total);
	}
}
