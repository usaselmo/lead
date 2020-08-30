package com.allscontracting.dto;

import com.allscontracting.model.Company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
	private Long id;
	private String name;
	private String email;
	private String address;
	private String website;

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
