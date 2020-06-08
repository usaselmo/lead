package com.allscontracting.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="client")
public class Client {

	@Id @GeneratedValue
	private Long id;
	
	@Email
	private String email;
	
	private String name;
	private String address;
	private String cellPhone;
	private String phone;
	
	/*@OneToMany(mappedBy="client", fetch=FetchType.LAZY) 
	private List<Lead> leads;*/

	public String getPhone() {
		return phone.substring(0, 3)+"-"+phone.substring(3, 6)+"-"+phone.substring(6);
	}

	public void setPhone(String phone) {
		this.phone = phone.replaceAll("\\(|\\)|\\-| ", "");
	}
	
}
