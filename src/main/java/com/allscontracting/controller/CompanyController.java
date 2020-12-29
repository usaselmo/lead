package com.allscontracting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("companies")
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

	@GetMapping
	public LeadEntity getCompanies() {
		return LeadEntity.builder().companies(this.companyService.getCompanies()).build();
	}

	@GetMapping("/search/{text}")
	public LeadEntity search(@PathVariable String text) {
		return LeadEntity.builder().companies(this.companyService.search(text)).build();
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

}
