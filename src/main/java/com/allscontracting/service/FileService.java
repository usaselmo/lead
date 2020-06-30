package com.allscontracting.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.EventType;
import com.allscontracting.event.LeadStatusChangeEvent;
import com.allscontracting.event.VendorFileLoadedEvent;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.tradutor.Translater;
import com.allscontracting.tradutor.TranslaterDispatcher;

@Service
public class FileService {
	@Autowired private LeadRepository leadRepo;
	@Autowired private TranslaterDispatcher tradutorFinder;
	@Autowired private EventManager eventManager;
	
/*	public void sendByEmail(Proposal proposal, String leadId) throws IOException {
		Lead lead = leadRepository.findOne(leadId);
		File proposalPdfFile = new File(PROPOSALS_FOLDER + "\\" + proposal.getFileName());
		Client client = lead.getClient();
		this.mailService.sendProposalByEmail(proposal, client, proposalPdfFile);
	}*/

	@SuppressWarnings("unchecked")
	public void loadLeadFile(MultipartFile file, Vendor vendor, Long userId) throws Exception {
		if (!tradutorFinder.dispatch(vendor).isFileFromRightVendor(file.getOriginalFilename(), vendor))
			throw new LeadsException("File and Vendor do not match.");
		Translater<Lead> translater = (Translater<Lead>) tradutorFinder.dispatch(vendor);
		List<Lead> leads = translater.vendorFileToLeads(file);
		if (leads.isEmpty())
			throw new LeadsException("Found no Leads in this file.");
		saveAllLeads(vendor, leads, userId);
	}

	@Transactional
	private void saveAllLeads(Vendor vendor, List<Lead> leads, Long userId) {
		leads.stream().forEach(lead->{
			if(!this.leadRepo.existsById(lead.getId())) {
				lead = leadRepo.save(lead);
				this.eventManager.notifyAllListeners(new VendorFileLoadedEvent(lead, vendor, userId));
				this.eventManager.notifyAllListeners(new LeadStatusChangeEvent(EventType.BEGIN.toString(), lead.getId(), userId) );
			}
		});
	}
	
}
