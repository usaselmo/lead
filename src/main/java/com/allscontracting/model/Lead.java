package com.allscontracting.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.allscontracting.event.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity(name="lead")
public class Lead implements Entity<String> {

	private static final long serialVersionUID = 2718925984228018742L;
	
	@Id
	private String id;
	
	static String VALUE = "";
		
	@Enumerated(EnumType.STRING)
	private Vendor vendor;
	
	@Temporal(value = TemporalType.DATE)
	private Date date;
	
	private String description;
	private BigDecimal fee;
	private String type;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private Client client;
	
  @OneToMany(mappedBy = "lead", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Proposal> proposals;

	@Enumerated(EnumType.STRING)
	private Event event ;
	
	public enum Vendor {
		HOME_ADVISOR, NETWORX
	}
	
}
