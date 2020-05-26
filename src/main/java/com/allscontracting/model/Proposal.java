package com.allscontracting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Table(name="proposal")
public class Proposal implements Entity<Long>, Comparable<Proposal>, Serializable {

	private static final long serialVersionUID = -8804397870000139075L;

	@Id
	@GeneratedValue
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

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "lead_id", insertable = false, updatable = false, nullable = false)
	private Lead lead;

  @OneToMany(mappedBy = "proposal", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Line> lines;
	
  @OneToMany(mappedBy = "proposal", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Item> items;
  
  boolean isFinished() {
  	return this.pdf != null;
  }

	@Override
	public int compareTo(Proposal o) {
    return this.id.compareTo(o.id);
	}
  	
}
