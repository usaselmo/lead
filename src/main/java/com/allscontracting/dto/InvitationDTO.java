package com.allscontracting.dto;

import com.allscontracting.exception.LeadsException;
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
	private CompanyDTO company;
	private LeadDTO lead;
	private Long number;
	
	public static final InvitationDTO of(Invitation invitation) {
		if(invitation==null)
			return null;
		return InvitationDTO.builder()
			.id(invitation.getId())
			.dueDate(Converter.dateToString(invitation.getDate(), Converter.MM_dd_yyyy_hh_mm))
			.company(CompanyDTO.of(invitation.getCompany()))
			.lead(LeadDTO.of(invitation.getLead()))
			.number(invitation.getNumber())
			.build();
	}
	
	public static final Invitation toInvitation(InvitationDTO i) throws LeadsException {
		Invitation invitation = new Invitation();
		invitation.setId(i.getId());
		invitation.setDueDate(Converter.convertToDate(i.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
		invitation.setCompany(CompanyDTO.toCompany(i.getCompany()));
		invitation.setLead(LeadDTO.toLead(i.getLead()));
		invitation.setNumber(i.getNumber());
		return invitation;
	}

}
