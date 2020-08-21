package com.allscontracting.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="item")
public class Item implements Entity<Long>{

	private static final long serialVersionUID = -7942592928182519301L;

	@Id	@GeneratedValue(strategy=GenerationType.AUTO)	private Long id;
	private String title;
	private BigDecimal price;

	@JsonIgnore
	@ManyToOne(cascade=CascadeType.ALL)
	private Proposal proposal;
	
  @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Line> lines;
  
  public void addLine(Line line) {
  	if(this.lines==null)
  		this.lines = new ArrayList<Line>();
  	if(!this.lines.contains(line)) {
  		this.lines.add(line);
  	}
  	line.setItem(this);
  }
	
}
