package com.allscontracting.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.Company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CompanyDTO {
	private final Long id;
	private final String name;
	private final String email;
	private final String address;
	private final String website;

	public static final CompanyDTO of(Company company) {
		if(company==null)
			return null;
		return CompanyDTO.builder()
			.id(company.getId())
			.address(company.getAddress())
			.email(company.getEmail())
			.name(company.getName())
			.website(company.getWebsite())
		.build();
	}
	
	public static final List<CompanyDTO> of(List<Company>  companies){
		return companies.stream().map(c-> CompanyDTO.of(c)).collect(Collectors.toList());
	}

	public static final Company toCompany(CompanyDTO cd) {
		if(cd==null)
			return null;
		Company c = new Company();
		c.setAddress(cd.getAddress());
		c.setEmail(cd.getEmail());
		c.setId(cd.getId());
		c.setName(cd.getName());
		//c.setUsers(users);
		c.setWebsite(cd.getWebsite());
		return c;
	}
}
