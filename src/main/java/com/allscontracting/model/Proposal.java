package com.allscontracting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

	//SINGLE PROPERTIES
	@Id	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id; 
	private Long number;
	private BigDecimal total;
	private String fileName;
	private String scopeOfWork;
	private boolean callMissUtility;
	private String paymentSchedule;
	private String workWarranty;
	private byte[] signedProposal;
	private boolean emailed;
	private String note;
	private Date date;
  @ManyToOne  @JoinColumn(name = "lead_id", insertable = true, updatable = false, nullable = false)	private Lead lead;
  
  //LISTS
  @OneToMany(mappedBy = "proposal", fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)	private List<Item> items;

  /************************
   * METHODS
   ************************/
  boolean isFinished() { 
  	return this.signedProposal != null; 
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
	
	public void removeItem(Item item) {
		if (this.items == null)
			this.items = new ArrayList<Item>();
		if(this.items.contains(item))
			this.items.remove(item);
		item.setProposal(null);
	}

	@Override
	public String toString() { 
		return "Proposal [id=" + id + ", number=" + number + ", total=" + total + ", scopeOfWork=" + scopeOfWork + ", callMissUtility=" + callMissUtility + ", paymentSchedule="
				+ paymentSchedule + ", workWarranty=" + workWarranty + ", emailed=" + emailed
				+ "]";
	}
	
}
