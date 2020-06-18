package com.allscontracting.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = 9167278336604403557L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;

	@Column(nullable = false, unique = true)
	private String email; 

	private String password;
	private String name;
	private boolean enabled;

}
