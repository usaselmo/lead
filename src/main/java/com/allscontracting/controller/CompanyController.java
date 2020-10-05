package com.allscontracting.controller;

import java.util.List;

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

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("companies")
@AllArgsConstructor
public class CompanyController {

	private final CompanyService companyService;

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

}
