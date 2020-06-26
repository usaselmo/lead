package com.allscontracting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.repo.CompanyRepository;

@Service
public class CompanyService {

	@Autowired CompanyRepository companyRepo;
	
	public List<CompanyDTO> getCompanies(){
		return this.companyRepo.findAll().stream().map(c->CompanyDTO.companyToDTO(c)).collect(Collectors.toList());
	}
	
}
