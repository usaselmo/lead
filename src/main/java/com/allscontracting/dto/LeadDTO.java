package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

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
	private Long visitDaysLeft;
	private String description;
	private String notes;
	private String event;//EventType
	private UserDTO estimator;
	private CompanyDTO company;
	private PersonDTO contact;
	private PersonDTO client;
	private String title;
	private String dueDate;
	private Long daysLeft;
	private String address;

	//LISTS
	private List<ProposalDTO> proposals;
	private List<EventLogDTO> eventLogs;
	private List<EventDTO> nextEvents;
	private List<MediaDTO> medias;
	private List<InvitationDTO> invitations;

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
				.vendor((lead.getVendor()!=null)?lead.getVendor().name():null)
				.description(lead.getDescription())
				.fee((lead.getFee()==null)?null:NumberFormat.getCurrencyInstance().format(lead.getFee())) 
				.type(lead.getType())
				.notes(lead.getNotes())
				.client(PersonDTO.of(lead.getClient()))
				.proposals(lead.getProposals()==null?null:lead.getProposals().stream().map(p->ProposalDTO.of(p)).collect(Collectors.toList()))
				.event((lead.getEvent()==null)?null:lead.getEvent().getStatus())
				.date( (lead.getDate()==null)?null:Converter.dateToString(lead.getDate(), Converter.dd_MM_yyyy) )
				.visit(lead.getVisit()!=null?Converter.dateToString(lead.getVisit(), Converter.MM_dd_yyyy_hh_mm):"")
				.visitDaysLeft(lead.getVisit()==null?null:TimeUnit.DAYS.convert(lead.getVisit().getTime() - new Date().getTime(), TimeUnit.MILLISECONDS))
				.dueDate(lead.getDueDate()!=null?Converter.dateToString(lead.getDueDate(), Converter.MM_dd_yyyy_hh_mm):"")
				.daysLeft(lead.getDueDate()==null?null:TimeUnit.DAYS.convert(lead.getDueDate().getTime() - new Date().getTime(), TimeUnit.MILLISECONDS) )
				.price(getTotalPrice(lead))
				.estimator(lead.getEstimator()==null?UserDTO.builder().build():UserDTO.of(lead.getEstimator()))
				.company(CompanyDTO.of(lead.getCompany()))
				.contact(PersonDTO.of(lead.getContact()))
				.title(lead.getTitle())
				.address(lead.getAddress())
				.medias(MediaDTO.of(lead.getMedias()))
				.invitations(InvitationDTO.of(lead.getInvitations()))
				.build();
	}
	
	private static long getTotalPrice(Lead lead) {
		if(lead.getProposals()==null || lead.getProposals().size()<=0)
			return BigDecimal.ZERO.longValue();
		
		List<Proposal> acceptedProposals = lead.getProposals().stream().filter(p->p.isAccepted()).collect(Collectors.toList());
		if(acceptedProposals!=null && acceptedProposals.size()>0) {
			return acceptedProposals.stream().map(Proposal::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add).longValue();
		}else {
			return lead.getProposals().stream().filter(p->!p.isChangeorder()).sorted(Comparator.reverseOrder()).findFirst().orElse(Proposal.builder().total(BigDecimal.ZERO).build()).getTotal().longValue();
		}
	}

	public static final Lead toLead(LeadDTO leadDTO) throws LeadsException {
		if(leadDTO==null)
			return null;
		Lead lead = new Lead();
		lead.setClient(PersonDTO.toPerson(leadDTO.getClient()));
		lead.setDate(Converter.convertToDate(leadDTO.getDate()));
		lead.setDescription(leadDTO.getDescription());
		lead.setEstimator(UserDTO.toUser(leadDTO.getEstimator()));
		lead.setEvent(StringUtils.isEmpty(leadDTO.getEvent())?null:Event.valueOf(leadDTO.getEvent()));
		lead.setFee(StringUtils.isEmpty(leadDTO.getFee())?BigDecimal.ZERO:new BigDecimal(leadDTO.getFee().replace("$", "")));
		lead.setId(StringUtils.isEmpty(leadDTO.getId())?null:Long.valueOf(leadDTO.getId()));
		lead.setOldid(leadDTO.getOldid());
		lead.setNotes(leadDTO.getNotes());
		lead.setProposals(leadDTO.getProposals()==null?null:leadDTO.getProposals().stream().map(p->ProposalDTO.toProposal(p)).collect(Collectors.toList()));
		lead.setType(leadDTO.getType());
		lead.setVendor(StringUtils.isEmpty(leadDTO.vendor)?null:Lead.Vendor.valueOf(leadDTO.vendor));
		lead.setVisit(Converter.convertToDate(leadDTO.getVisit()));
		lead.setDueDate(Converter.convertToDate(leadDTO.getDueDate()));
		lead.setCompany(CompanyDTO.toCompany(leadDTO.getCompany()));
		lead.setContact(PersonDTO.toPerson(leadDTO.getContact()));
		lead.setTitle(leadDTO.getTitle());
		lead.setAddress(leadDTO.getAddress());
		lead.setMedias(leadDTO.getMedias().stream().map(media->MediaDTO.toMedia(media)).collect(Collectors.toList()));
		lead.setInvitations(InvitationDTO.toInvitation(leadDTO.getInvitations()));
		return lead;
	}

}
