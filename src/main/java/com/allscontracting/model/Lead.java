package com.allscontracting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.allscontracting.event.EventType;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table(name="lead2")
public class Lead implements Serializable {

	private static final long serialVersionUID = -6183199069186068646L;

	@Id	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String oldid; 
	
	@Enumerated(EnumType.STRING)
	private Vendor vendor;

	@Temporal(value = TemporalType.DATE)
	private Date date;

	private String description;
	private BigDecimal fee;
	private String type;
	private String notes;

	@ManyToOne(cascade = CascadeType.ALL)
	private Client client;

  @OneToMany(mappedBy = "lead", fetch = FetchType.LAZY)
	private List<Proposal> proposals;

  @NotNull
	@Enumerated(EnumType.STRING)
	private EventType event;
  
	@ManyToOne(cascade = CascadeType.ALL)
  private User estimator;

	public enum Vendor {
		HOME_ADVISOR, NETWORX, PHONE_CALL, EMAIL
	}

	@Temporal(TemporalType.TIMESTAMP)
	private Date visit;

	/* TODO public void addProposal (Proposal proposal) {
		if(this.proposals == null)
			this.proposals = new ArrayList<Proposal>();
		if(!this.proposals.contains(proposal)) {
			this.proposals.add(proposal);
			if(!proposal.getLead().equals(this))
				proposal.setLead(this);
		}
	}*/
	
	public void removeProposal(Proposal proposal) {
		if(this.proposals != null) {
			if(this.proposals.contains(proposal))
				this.proposals.remove(proposal);
			if(proposal.getLead().equals(this))
				proposal.setLead(null);
		}
	}

	public void setNote(String note) throws Exception {
		throw new Exception();
	}
	
	public void addNote(String note) {
		this.notes = 
				note = new StringBuilder()
					.append(Converter.dateToString(new Date()))
					.append(System.lineSeparator())
					.append(note)
					.append(System.lineSeparator())
					.append(System.lineSeparator()) 
					.append((!StringUtils.isEmpty(this.notes)? this.notes:"")  ) 
					.toString();
	}
	
}
