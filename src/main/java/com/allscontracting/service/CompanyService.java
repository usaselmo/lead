package com.allscontracting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.allscontracting.dto.CompanyDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Company;
import com.allscontracting.repo.CompanyRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyService {

	private CompanyRepository companyRepo;

	public List<CompanyDTO> getCompanies() {
		return this.companyRepo.findAll().stream().map(c -> CompanyDTO.of(c)).collect(Collectors.toList());
	}

	public CompanyDTO update(CompanyDTO companyDTO) throws LeadsException {
		Company company = this.companyRepo.findById(companyDTO.getId()).orElseThrow(() -> new LeadsException("Person not found"));
		company.setAddress(companyDTO.getAddress());
		company.setEmail(companyDTO.getEmail());
		company.setName(companyDTO.getName());
		company.setWebsite(companyDTO.getWebsite());
		return CompanyDTO.of(this.companyRepo.save(company));
	}

	public CompanyDTO save(CompanyDTO companyDTO) {
		Company company = CompanyDTO.toCompany(companyDTO);
		return CompanyDTO.of(this.companyRepo.save(company));
	}

}
