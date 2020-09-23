package com.allscontracting.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.Invitation;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {

	private Long id;
	private String date;
	private String dueDate;
	private LeadDTO lead;
	private CompanyDTO company;
	private PersonDTO contact;
	private List<MediaDTO> medias;
	private List<MediaDTO> proposals;
	private Long emailed;
	private BigDecimal price;
	
	public static final InvitationDTO of(Invitation invitation) {
		if(invitation==null)
			return null;
		return InvitationDTO.builder()
			.id(invitation.getId())
			.dueDate(Converter.dateToString(invitation.getDueDate(), Converter.MM_dd_yyyy_hh_mm))
			.date(Converter.dateToString(invitation.getDate(), Converter.MM_dd_yyyy_hh_mm))
			.company(CompanyDTO.of(invitation.getCompany()))
			//.lead(LeadDTO.of(invitation.getLead()))
			.medias(MediaDTO.of(invitation.getMedias()))
			.proposals(MediaDTO.of(invitation.getProposals()))
			.contact(PersonDTO.of(invitation.getContact()))
			.emailed(invitation.getEmailed()==null?0L:invitation.getEmailed())
			.price(invitation.getPrice())
			.build();
	}
	
	public static final List<InvitationDTO> of(List<Invitation> invitations){
		if(invitations==null || invitations.size()==0) return null;
		return invitations.stream().map(inv->InvitationDTO.of(inv)).collect(Collectors.toList());
	}
	
	public static final List<Invitation> toInvitation(List<InvitationDTO> invitationDTOs){
		if(invitationDTOs==null || invitationDTOs.size()==0) return null;
		return invitationDTOs.stream().map(inv->InvitationDTO.toInvitation(inv)).collect(Collectors.toList());
	}
	
	public static final Invitation toInvitation(InvitationDTO invitationDTO) {
		if(invitationDTO==null) 
			return null;
		Invitation invitation = new Invitation();
		invitation.setId(invitationDTO.getId());
		invitation.setDueDate(Converter.convertToDate(invitationDTO.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
		invitation.setDate(Converter.convertToDate(invitationDTO.getDate(), Converter.MM_dd_yyyy_hh_mm));
		invitation.setCompany(CompanyDTO.toCompany(invitationDTO.getCompany()));
		//invitation.setLead(LeadDTO.toLead(i.getLead()));
		invitation.setMedias(MediaDTO.toMedia(invitationDTO.getMedias()));
		invitation.setProposals(MediaDTO.toMedia(invitationDTO.getProposals()));
		invitation.setContact(PersonDTO.toPerson(invitationDTO.getContact()));
		invitation.setEmailed(invitationDTO.getEmailed()==null?0L:invitationDTO.getEmailed());
		invitation.setPrice(invitationDTO.getPrice());
		return invitation;
	}

}
