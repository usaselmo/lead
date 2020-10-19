package com.allscontracting.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.allscontracting.model.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PersonDTO {

	private final String id;
	private final String email;
	private final String name;
	private final String address;
	private final String cellPhone;
	private final String phone;
	private final CompanyDTO company;

	public static final PersonDTO of(Person person) {
		if (person == null)
			return null;
		return PersonDTO.builder().id(String.valueOf(person.getId())).email(person.getEmail()).name(person.getName()).address(person.getAddress()).cellPhone(person.getCellPhone())
				.company(CompanyDTO.of(person.getCompany()))
				.phone(person.getPhone()).build();
	}

	public static final Person toPerson(PersonDTO cd) {
		if(cd==null)
			return null;
		Person c = new Person();
		c.setAddress(cd.getAddress());
		c.setCellPhone(cd.getCellPhone());
		c.setEmail(cd.getEmail());
		c.setId(StringUtils.isEmpty(cd.getId()) ? null : Long.valueOf(cd.getId()));
		c.setName(cd.getName());
		c.setPhone(cd.getPhone());
		c.setCompany(CompanyDTO.toCompany(cd.getCompany()));
		return c;
	}

	public static List<PersonDTO> of(List<Person> persons) {
		return persons.stream().map(person -> of(person)).collect(Collectors.toList());
	}
	
	public static final List<Person> toPerson(List<PersonDTO> dtos) {
		if(null==dtos) return null;
		return dtos.stream().map(dto -> PersonDTO.toPerson(dto)).collect(Collectors.toList());
	}
}
