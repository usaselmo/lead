package com.allscontracting.dto;

import com.allscontracting.model.Company;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompanyDTO {
	private Long id;
	private String name;
	private String email;
	private String address;
	private String website;

	public static final CompanyDTO companyToDTO(Company company) {
		return CompanyDTO.builder()
			.id(company.getId())
			.address(company.getAddress())
			.email(company.getEmail())
			.name(company.getName())
			.website(company.getWebsite())
		.build();
	}
}
