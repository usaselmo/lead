package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.event.EventType;
import com.allscontracting.exception.LeadsException;
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
	private String oldid;
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
				.oldid(lead.getOldid())
				.vendor(lead.getVendor().toString())
				.date(Converter.dateToString(lead.getDate()))
				.description(lead.getDescription())
				.fee(NumberFormat.getCurrencyInstance().format(lead.getFee())) 
				.type(lead.getType())
				.notes(lead.getNotes())
				.client(ClientDTO.of(lead.getClient()))
				.proposals(lead.getProposals()==null?null:lead.getProposals().stream().map(p->ProposalDTO.of(p)).collect(Collectors.toList()))
				.event(lead.getEvent().toString())
				.visit(lead.getVisit()!=null?Converter.dateToString(lead.getVisit()):"")
				.lastProposalTotal(  getLastProposalTotal(lead)  )
				.estimator(lead.getEstimator()==null?new UserDTO():UserDTO.of(lead.getEstimator()))
				.build();
	}
	
	public static final Lead toLead(LeadDTO leadDTO) throws LeadsException {
		Lead lead = new Lead();
		lead.setClient(ClientDTO.toClient(leadDTO.getClient()));
		lead.setDate(Converter.convertToDate(leadDTO.getDate()));
		lead.setDescription(leadDTO.getDescription());
		lead.setEstimator(UserDTO.toUser(leadDTO.getEstimator()));
		lead.setEvent(StringUtils.isBlank(leadDTO.getEvent())?null:EventType.valueOf(leadDTO.getEvent()));
		lead.setFee(StringUtils.isBlank(leadDTO.getFee())?BigDecimal.ZERO:new BigDecimal(leadDTO.getFee().replace("$", "")));
		lead.setId(StringUtils.isBlank(leadDTO.getId())?null:Long.valueOf(leadDTO.getId()));
		lead.setOldid(leadDTO.getOldid());
		lead.setNotes(leadDTO.getNotes());
		lead.setProposals(leadDTO.getProposals()==null?null:leadDTO.getProposals().stream().map(p->ProposalDTO.toProposal(p)).collect(Collectors.toList()));
		lead.setType(leadDTO.getType());
		lead.setVendor(StringUtils.isBlank(leadDTO.vendor)?null:Lead.Vendor.valueOf(leadDTO.vendor));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit()));
		return lead;
	}

	private static String getLastProposalTotal(Lead lead) {
		if(lead.getProposals()==null)
			return null;
		BigDecimal total = lead.getProposals().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(Proposal.builder().total(BigDecimal.ZERO).build()).getTotal();
		if (total == null || total.equals(BigDecimal.ZERO))
			return null;
		return NumberFormat.getCurrencyInstance().format(total);
	}
	
}
