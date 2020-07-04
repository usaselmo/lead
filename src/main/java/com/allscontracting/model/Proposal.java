package com.allscontracting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="proposal2")
public class Proposal implements Entity<Long>, Comparable<Proposal>, Serializable {

	private static final long serialVersionUID = -8804397870000139075L;

	@Id	
	private Long id; 
	
	private Long number;
	private BigDecimal total;
	private String fileName;
	private String scopeOfWork;
	private boolean callMissUtility;
	private String paymentSchedule;
	private String workWarranty;
	private byte[] pdf;
	private boolean emailed;
	private String note;

	@JsonIgnore
  @ManyToOne 
  @JoinColumn(name = "lead_id", insertable = true, updatable = false, nullable = false)
	private Lead lead;
	
  @OneToMany(mappedBy = "proposal", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Item> items;
  
  boolean isFinished() { 
  	return this.pdf != null; 
  }

	@Override
	public int compareTo(Proposal o) {
    return this.id.compareTo(o.id);
	}

	public void addItem(Item item) {
		if (this.items == null)
			this.items = new ArrayList<Item>();
		if (!this.items.contains(item)) {
			this.items.add(item);
		}
		item.setProposal(this);
	}

	@Override
	public String toString() { 
		return "Proposal [id=" + id + ", number=" + number + ", total=" + total + ", scopeOfWork=" + scopeOfWork + ", callMissUtility=" + callMissUtility + ", paymentSchedule="
				+ paymentSchedule + ", workWarranty=" + workWarranty + ", emailed=" + emailed
				+ "]";
	}

	
}
