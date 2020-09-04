package com.allscontracting.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.allscontracting.dto.InvitationDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Media;
import com.allscontracting.repo.InvitationRepo;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.MediaRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationService {

	private final InvitationRepo invitationRepo;
	private final LeadRepository leadRepo;
	private final MediaRepo mediaRepo;
	
	@Transactional
	public InvitationDTO save(InvitationDTO invitationDTO, Long leadId) throws LeadsException {
		Lead lead = leadRepo.findById(leadId).orElseThrow( () -> new LeadsException("Could not find Lead"));

		Invitation invitation = InvitationDTO.toInvitation(invitationDTO);
		invitation.setDate(new Date());
		invitation.setLead(lead);
		invitation.setMedias(invitation.getMedias().stream().map(m->mediaRepo.findById(m.getId()).orElse(null)).collect(Collectors.toList()));
		
		System.out.println(lead.getId()+" - "+ invitation.getCompany().getId());
		invitation = this.invitationRepo.save(invitation);

		lead.addInvitation(invitation);
		leadRepo.save(lead);
		return InvitationDTO.of(invitation);
	}

}
