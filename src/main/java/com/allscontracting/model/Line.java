package com.allscontracting.model;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.allscontracting.type.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity(name = "line")
@Table(name = "line")
public class Line implements Entity<Long> {

	private static final long serialVersionUID = -8805126155137619614L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String description;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private Item item;

	@Override
	public String toString() {
		return "Line [id=" + id + ", description=" + description + ", item=" + item + "]";
	}
	
}
