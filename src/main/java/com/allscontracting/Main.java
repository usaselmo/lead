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
	}

/*	@Transactional
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
