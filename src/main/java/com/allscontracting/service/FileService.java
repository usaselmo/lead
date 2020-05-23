package com.allscontracting.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.event.EventManager;
import com.allscontracting.event.VendorFileLoadedEvent;
import com.allscontracting.model.Client;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.repo.LeadRepository;
import com.allscontracting.model.Proposal;
import com.allscontracting.tradutor.Translater;
import com.allscontracting.tradutor.TranslaterDispatcher;

@Service
public class FileService {
	@Autowired private LeadRepository leadRepo;
	@Autowired private TranslaterDispatcher tradutorFinder;
	@Autowired private EventManager eventManager;
	@Autowired private MailService mailService;
	@Autowired private LeadRepository leadRepository;
	
	public void sendByEmail(Proposal proposal, String leadId) throws IOException {
		Lead lead = leadRepository.findOne(leadId);
		File proposalPdfFile = new File(PROPOSALS_FOLDER + "\\" + proposal.getFileName());
		Client client = lead.getClient();
		this.mailService.sendProposalByEmail(proposal, client, proposalPdfFile);
	}

	@SuppressWarnings("unchecked")
	public void loadLeadFile(MultipartFile file, Vendor vendor) throws Exception {
		if(!tradutorFinder.dispatch(vendor).isFileFromRightVendor(file.getOriginalFilename(), vendor))
			throw new Exception("File and Vendor do not match.");
		List<String> lines = Arrays.asList(new String(file.getBytes()).split(System.lineSeparator()));
		Translater<Lead> translater = (Translater<Lead>) tradutorFinder.dispatch(vendor);
		List<Lead> leads = lines.stream() 
				.map(line->line.replaceAll("\\r|\\n", ""))
				.map(line -> translater.importedFileLineToEntity(line, Lead.class))
				.filter(lead -> !StringUtils.isEmpty(lead.getId()))
				.collect(Collectors.toList());
		if(leads.isEmpty())
			throw new Exception("Found no Leads in this file. Make sure ';' is the file delimiter. "); 
		//save all
		leads.stream().forEach(lead->{
			if(!this.leadRepo.exists(lead.getId())) {
				this.leadRepo.save(lead);
				this.eventManager.notifyAllListeners(new VendorFileLoadedEvent(lead, vendor));
			}
		});
	}

	private static final Path PROPOSALS_FOLDER = Paths.get("C:\\Users\\Anselmo.asr\\Google Drive\\All's Contracting\\proposals");

	public List<Proposal> findLeadProposals(String id) throws IOException {

		//Lead lead = this.leadRepo.findOne(id).orElse(Lead.builder().build());
		Lead lead = this.leadRepo.findOne(id);
		File folder = PROPOSALS_FOLDER.toFile();
		
		return Stream.of(folder.list())
			.filter(file->file.toString().startsWith(lead.getClient().getName()) && file.toString().endsWith(".pdf"))
			.map(file->Proposal.builder().id(38974893L).fileName(file.toString()).build())
			.collect(Collectors.toList());

		//return Arrays.asList(Proposal.builder().total(BigDecimal.valueOf(120.36)).build());
	}
	
}
