package com.allscontracting.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name="company")
public class Company implements Serializable{
	
	private static final long serialVersionUID = -8940683804972801566L;

	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	private String name;
	private String email;
	private String address;
	private String website;
	
	@OneToMany(fetch=FetchType.LAZY) private List<User> users;
	
}
