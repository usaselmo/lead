package com.allscontracting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.CompanyDTO;
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

	@GetMapping("byName")
	public List<CompanyDTO> getCompaniesByName(@RequestParam String companyName){
		return this.companyService.findLikeName(companyName);
	}
	
	@PutMapping
	public CompanyDTO updateCompany(@RequestBody CompanyDTO companyDTO) throws LeadsException {
		return companyService.update(companyDTO);
	}

}