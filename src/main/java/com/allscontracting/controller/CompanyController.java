package com.allscontracting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.CompanyDTO;
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
}
