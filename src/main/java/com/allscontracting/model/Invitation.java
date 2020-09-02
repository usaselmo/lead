package com.allscontracting.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table
public class Invitation implements Serializable {

	private static final long serialVersionUID = 5702457065775817600L;
	@Id	@GeneratedValue(strategy=GenerationType.AUTO)	private Long id;
	@Temporal(TemporalType.TIMESTAMP)	private Date date;
	@Temporal(TemporalType.TIMESTAMP)	private Date dueDate;
	@ManyToOne private Company company;
	@ManyToOne private Lead lead;
	private Long number;
	
	
	
}
