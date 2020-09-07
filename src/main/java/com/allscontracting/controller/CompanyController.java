package com.allscontracting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.service.CompanyService;

@RestController
@RequestMapping("companies")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@GetMapping
	public List<CompanyDTO> getCompanies() {
		return this.companyService.getCompanies();
	}
	
	@PutMapping
	public LeadEntity update(@RequestBody CompanyDTO companyDTO) throws LeadsException {
		try {
			return LeadEntity.builder().company(companyService.update(companyDTO)).build().addSuccessMessage("Company created.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not update Company.");
		}
	}
	
	@PostMapping
	public LeadEntity save(@RequestBody CompanyDTO companyDTO) throws LeadsException {
		try {
			return LeadEntity.builder().company(companyService.save(companyDTO)).build().addSuccessMessage("Company created.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage("Could not save Company.");
		}
	}
	
	
	
	
	
	
	
	
	

	/*
	 * @GetMapping("byName") public List<CompanyDTO>
	 * getCompaniesByName(@RequestParam String companyName){ return
	 * this.companyService.findLikeName(companyName); }
	 */

}
