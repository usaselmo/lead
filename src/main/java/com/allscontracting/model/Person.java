package com.allscontracting.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name = "person")
public class Person implements Serializable, Client {

	private static final String REGEX = "\\(|\\)|\\-|\\.| ";

	private static final long serialVersionUID = -2053753047318969493L;

	@Id
	@GeneratedValue
	private Long id;
	private String email;
	private String name;
	private String address;
	private String cellPhone;
	private String phone;

	@ManyToOne
	private Company company;

	public String getPhone() {
		return StringUtils.isEmpty(phone) ? "" : phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
	}

	public void setCellPhone(String phone) {
		this.cellPhone = StringUtils.isEmpty(phone) ? null : phone.replaceAll(REGEX, "");
	}

	public void setPhone(String phone) {
		this.phone = !StringUtils.isEmpty(phone) ? phone.replaceAll(REGEX, "") : "";
	}

}
