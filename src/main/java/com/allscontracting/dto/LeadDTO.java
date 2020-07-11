package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
	private UserDTO estimator;
	
	public static final LeadDTO of(Lead lead) {
		if(lead==null)
			return LeadDTO.builder().build();
		return LeadDTO.builder()
				.id(String.valueOf(lead.getId()))
				.vendor(lead.getVendor().toString())
				.date(Converter.dateToString(lead.getDate()))
				.description(lead.getDescription())
				.fee(NumberFormat.getCurrencyInstance().format(lead.getFee())) 
				.type(lead.getType())
				.notes(lead.getNotes())
				.client(ClientDTO.of(lead.getClient()))
				.proposals(lead.getProposals().stream().map(p->ProposalDTO.of(p)).collect(Collectors.toList()))
				.event(lead.getEvent().toString())
				.visit(lead.getVisit()!=null?Converter.dateToString(lead.getVisit()):"")
				.lastProposalTotal(  getLastProposalTotal(lead)  )
				.estimator(lead.getEstimator()==null?new UserDTO():UserDTO.of(lead.getEstimator()))
				.build();
	}

	private static String getLastProposalTotal(Lead lead) {
		BigDecimal total = lead.getProposals().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(Proposal.builder().total(BigDecimal.ZERO).build()).getTotal();
		if (total == null || total.equals(BigDecimal.ZERO))
			return null;
		return NumberFormat.getCurrencyInstance().format(total);
	}
	
}
