package com.allscontracting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Company;
import com.allscontracting.repo.CompanyRepository;

@Service
public class CompanyService {

	@Autowired CompanyRepository companyRepo;
	
	public List<CompanyDTO> getCompanies(){
		return this.companyRepo.findAll().stream().map(c->CompanyDTO.companyToDTO(c)).collect(Collectors.toList());
	}

	public List<CompanyDTO> findLikeName(String companyName) {
		return this.companyRepo.findLikeName(companyName).stream().map(c->CompanyDTO.companyToDTO(c)).collect(Collectors.toList());
	}

	public CompanyDTO update(CompanyDTO companyDTO) throws LeadsException {
		Company company = this.companyRepo.findById(companyDTO.getId()).orElseThrow(()->new LeadsException("Client not found"));
		company.setAddress(companyDTO.getAddress());
		company.setEmail(companyDTO.getEmail());
		company.setName(companyDTO.getName());
		company.setWebsite(companyDTO.getWebsite());
		return CompanyDTO.companyToDTO(this.companyRepo.save(company));
	}
	
}
