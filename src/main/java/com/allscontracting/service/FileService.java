package com.allscontracting.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Media;
import com.allscontracting.repo.InvitationRepo;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.MediaRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final LeadRepository leadRepo;
	private final MediaRepo mediaRepo;
	private final InvitationRepo invitationRepo;

	@Transactional
	public void storeLeadMedia(MultipartFile file, Long leadId) {
		Optional<Lead> otionalLead = leadRepo.findById(leadId);
		if (otionalLead.isPresent()) {
			try {
				Lead lead = otionalLead.get();
				Media media = new Media(null, file.getBytes(), file.getContentType(), file.getOriginalFilename());
				media = this.mediaRepo.save(media);
				lead.addMedia(media);
				leadRepo.save(lead);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Transactional
	public void uploadInvitationProposal(Long invitationId, MultipartFile file) {
		Optional<Invitation> optionalInv = invitationRepo.findById(invitationId);
		if (optionalInv.isPresent()) {
			try {
				Invitation inv = optionalInv.get();
				Media media = new Media(null, file.getBytes(), file.getContentType(), file.getOriginalFilename());
				media = this.mediaRepo.save(media);
				inv.addProposal(media);
				invitationRepo.save(inv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
