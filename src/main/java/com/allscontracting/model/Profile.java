package com.allscontracting.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;

import lombok.Data;

@Data
@Entity
@Table(name="profile")
public class Profile implements Serializable{
	private static final long serialVersionUID = 301333194976675168L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	@Max(value=128) private String description;
	
	public enum Description {
		GUEST, USER, ESTIMATOR, MANAGER, ADMIN;
	}
	
}
