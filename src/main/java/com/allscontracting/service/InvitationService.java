package com.allscontracting.service;

import java.util.Date;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.allscontracting.dto.InvitationDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.User;
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
	private final LogService logService;
	
	@Transactional
	public InvitationDTO save(InvitationDTO invitationDTO, Long leadId, User user) throws LeadsException {
		Lead lead = leadRepo.findById(leadId).orElseThrow( () -> new LeadsException("Could not find Lead"));

		Invitation invitation = InvitationDTO.toInvitation(invitationDTO);
		invitation.setDate(new Date());
		invitation.setLead(lead);
		invitation.setMedias(invitation.getMedias().stream().map(m->mediaRepo.findById(m.getId()).orElse(null)).collect(Collectors.toList()));
		
		invitation = this.invitationRepo.save(invitation);

		lead.addInvitation(invitation);
		leadRepo.save(lead);
		logService.newInvitationCreated(lead.getId(), invitation, user);
		return InvitationDTO.of(invitation);
	}

	public void deleteInvitation(Long leadId, Long invitaionId, User user) throws LeadsException {
		Lead lead = leadRepo.findById(leadId).orElseThrow( ()-> new LeadsException("Could not find Lead"));
		Invitation invitation = invitationRepo.findById(invitaionId).orElseThrow( ()-> new LeadsException("Could not find Invitation"));
		lead.removeInvitation(invitation);
		invitationRepo.delete(invitation);
		leadRepo.save(lead);
	}

}
