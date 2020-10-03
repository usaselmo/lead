package com.allscontracting.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.model.Media;
import com.allscontracting.model.User;
import com.allscontracting.repo.InvitationRepo;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.repo.MediaRepo;
import com.allscontracting.tradutor.Translater;
import com.allscontracting.tradutor.TranslaterDispatcher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final LeadRepository leadRepo;
	private final TranslaterDispatcher tradutorFinder;
	private final LogService logService;
	private final MediaRepo mediaRepo;
	private final InvitationRepo invitationRepo;

	@SuppressWarnings("unchecked")
	public void loadLeadFile(MultipartFile file, Vendor vendor, User user) throws Exception {
		if (!tradutorFinder.dispatch(vendor).isFileFromRightVendor(file.getOriginalFilename(), vendor))
			throw new LeadsException("File and Vendor do not match.");
		Translater<Lead> translater = (Translater<Lead>) tradutorFinder.dispatch(vendor);
		List<Lead> leads = translater.vendorFileToLeads(file);
		if (leads.isEmpty())
			throw new LeadsException("Found no Leads in this file.");
		saveAllLeads(vendor, leads, user);
	}

	@Transactional
	private void saveAllLeads(Vendor vendor, List<Lead> leads, User user) {
		leads.stream().forEach(lead->{
			List<Lead> fls = leadRepo.findByVDD(lead.getVendor(), lead.getDate(), lead.getDescription(), lead.getClient().getName(), lead.getOldid());
			if(fls == null || fls.size() <= 0) {
				lead = leadRepo.save(lead);
				logService.event(Lead.class, String.valueOf(lead.getId()), Event.LOAD_VENDOR_FILE, user);
			}
		});
	}

	@Transactional
	public void storeLeadMedia(MultipartFile file, Long leadId) {
		Optional<Lead> otionalLead = leadRepo.findById(leadId);
		if(otionalLead.isPresent()) {
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
		if(optionalInv.isPresent()) {
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
