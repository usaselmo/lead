package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.event.Event;
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
	
	//SINGLE PROPERTIES
	private String id;
	private String date;
	private String visit;
	private String description;
	private String notes;
	private String event;//EventType
	private UserDTO estimator;
	private CompanyDTO company;
	private PersonDTO contact;
	private PersonDTO client;
	private List<EventLogDTO> eventLogs;
	private String title;

	//LISTS
	private List<ProposalDTO> proposals;

	//DEPRECATED
	private String oldid;
	private String fee;
	private String type; 
	private String vendor;

	//TRANSIENTS OR CALCULATED
	private Long price;
	
	public static final LeadDTO of(Lead lead) {
		if(lead==null)
			return null;
		return LeadDTO.builder()
				.id(String.valueOf(lead.getId()))
				.oldid(lead.getOldid())
				.vendor(lead.getVendor().toString())
				.date(Converter.dateToString(lead.getDate()))
				.description(lead.getDescription())
				.fee(NumberFormat.getCurrencyInstance().format(lead.getFee())) 
				.type(lead.getType())
				.notes(lead.getNotes())
				.client(PersonDTO.of(lead.getClient()))
				.proposals(lead.getProposals()==null?null:lead.getProposals().stream().map(p->ProposalDTO.of(p)).collect(Collectors.toList()))
				.event(lead.getEvent().getStatus())
				.visit(lead.getVisit()!=null?Converter.dateToString(lead.getVisit()):"")
				.price(getTotalPrice(lead))
				.estimator(lead.getEstimator()==null?new UserDTO():UserDTO.of(lead.getEstimator()))
				.company(CompanyDTO.of(lead.getCompany()))
				.contact(PersonDTO.of(lead.getContact()))
				.title(lead.getTitle())
				.build();
	}
	
	private static long getTotalPrice(Lead lead) {
		if(lead.getProposals()==null || lead.getProposals().size()<=0)
			return BigDecimal.ZERO.longValue();
		BigDecimal total = lead.getProposals().stream().sorted(Comparator.reverseOrder()).findFirst().orElse(Proposal.builder().total(BigDecimal.ZERO).build()).getTotal();
		return total.longValue();
	}

	public static final Lead toLead(LeadDTO leadDTO) throws LeadsException {
		if(leadDTO==null)
			return null;
		Lead lead = new Lead();
		lead.setClient(PersonDTO.toPerson(leadDTO.getClient()));
		lead.setDate(Converter.convertToDate(leadDTO.getDate()));
		lead.setDescription(leadDTO.getDescription());
		lead.setEstimator(UserDTO.toUser(leadDTO.getEstimator()));
		lead.setEvent(StringUtils.isBlank(leadDTO.getEvent())?null:Event.valueOf(leadDTO.getEvent()));
		lead.setFee(StringUtils.isBlank(leadDTO.getFee())?BigDecimal.ZERO:new BigDecimal(leadDTO.getFee().replace("$", "")));
		lead.setId(StringUtils.isBlank(leadDTO.getId())?null:Long.valueOf(leadDTO.getId()));
		lead.setOldid(leadDTO.getOldid());
		lead.setNotes(leadDTO.getNotes());
		lead.setProposals(leadDTO.getProposals()==null?null:leadDTO.getProposals().stream().map(p->ProposalDTO.toProposal(p)).collect(Collectors.toList()));
		lead.setType(leadDTO.getType());
		lead.setVendor(StringUtils.isBlank(leadDTO.vendor)?null:Lead.Vendor.valueOf(leadDTO.vendor));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit()));
		lead.setCompany(CompanyDTO.toCompany(leadDTO.getCompany()));
		lead.setContact(PersonDTO.toPerson(leadDTO.getContact()));
		lead.setTitle(leadDTO.getTitle());
		return lead;
	}

}
