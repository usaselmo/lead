package com.allscontracting;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Main.class, arguments); 
	}

	
	@Override
	public void run(String... args) throws Exception {
		//populateProposal2Table();
	}
	
	
	/*@Autowired ProposalRepository oldProposalRepo;
	@Autowired Proposal2Repository newProposalRepo;
	@Autowired LeadRepository leadRepo;
	
	@Transactional
	private void populateProposal2Table() {
		long oldTotal = oldProposalRepo.count();
		List<Proposal> oldProposals = oldProposalRepo.findAll();
		for (Proposal oldProposal : oldProposals) {
			String oldLeadId = oldProposal.getLead_id();
			if(!StringUtils.isBlank(oldLeadId)) {
				Optional<Lead> lead = leadRepo.findByOldId(oldLeadId);
				if(lead.isPresent()) {
					Proposal2 newProposal = new Proposal2();
					newProposal.setCallMissUtility(oldProposal.isCallMissUtility());
					newProposal.setEmailed(oldProposal.isEmailed());
					newProposal.setFileName(oldProposal.getFileName());
					newProposal.setId(oldProposal.getId());
					//newProposal.setItems(oldProposal.getItems());
					newProposal.setLead(lead.get());
					newProposal.setNote(oldProposal.getNote());
					newProposal.setNumber(oldProposal.getNumber());
					newProposal.setPaymentSchedule(oldProposal.getPaymentSchedule());
					newProposal.setPdf(oldProposal.getPdf());
					newProposal.setScopeOfWork(oldProposal.getScopeOfWork());
					newProposal.setTotal(oldProposal.getTotal());
					newProposal.setWorkWarranty(oldProposal.getWorkWarranty());
					newProposalRepo.save(newProposal);
				}
			}
			
		}
		oldProposalRepo.findAll().stream()
			.forEach(oldProposal->{
			});
		
		long newTotal = newProposalRepo.count();
		System.out.println("old Proposal Total: "+oldTotal+" - new Proposal Total: "+newTotal);
	}*/

	/*@Transactional
	private void populateLead2Table() {
		long velhoTotal = velho.count();
		List<Lead> velhos = velho.findAll();
		velhos.stream()
			.forEach(v->{
				Lead2 n = new Lead2();
				n.setClient(v.getClient());
				n.setDate(v.getDate());
				n.setDescription(v.getDescription());
				n.setEstimator(v.getEstimator());
				n.setEvent(v.getEvent());
				n.setFee(v.getFee());
				n.setNotes(v.getNotes());
				n.setOldid(v.getId());
				//n.setProposals(v.getProposals());
				n.setType(v.getType());
				n.setVendor(Lead2.Vendor.valueOf(v.getVendor().toString()));
				n.setVisit(v.getVisit());
				novo.save(n);
			});
		long novoTotal = novo.count();
		System.out.println("velho: "+velhoTotal+" - novo: "+novoTotal);
		System.out.println("Termino");
	}*/

}
