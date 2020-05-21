package com.allscontracting.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity(name="client")
public class Client {

	@Id @GeneratedValue
	private Long id;
	
	private String name;
	private String address;
	private String cellPhone;
	private String phone;
	
	/*@OneToMany(mappedBy="client", fetch=FetchType.LAZY) 
	private List<Lead> leads;*/
	
	@Email
	private String email;
	
}
