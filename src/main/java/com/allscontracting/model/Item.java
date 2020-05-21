package com.allscontracting.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
public class Item implements Entity<Long>{

	private static final long serialVersionUID = -7942592928182519301L;

	@Id @GeneratedValue
	private Long id;
	
	private String title;

	@ManyToOne
	private Proposal proposal;
	
  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Line> lines;
	
}
