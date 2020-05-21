package com.allscontracting.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity(name = "line")
public class Line implements Entity<Long> {

	private static final long serialVersionUID = -8805126155137619614L;

	@Id
	@GeneratedValue
	private Long id;

	private String description;

	@ManyToOne
	private Proposal proposal;

	@ManyToOne
	private Item item;

}
