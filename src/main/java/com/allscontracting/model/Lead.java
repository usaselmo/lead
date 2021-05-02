package com.allscontracting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.util.StringUtils;

import com.allscontracting.event.Event;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lead2")
public class Lead implements Serializable {

	private static final long serialVersionUID = -6183199069186068646L;

	// SINGLE PROPERTIES
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Temporal(TemporalType.DATE)
	private Date date;
	@Temporal(TemporalType.TIMESTAMP)
	private Date visit;
	private String description;
	private String notes;
	private String title;
	private String address;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	@Enumerated(EnumType.STRING)
	private Event event;
	@ManyToOne
	private User estimator;

	@ManyToOne
	private Company company;
	@ManyToOne
	private Person contact;
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Person client;

	// LISTS
	@OneToMany(mappedBy = "lead", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Proposal> proposals;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinTable(name = "lead_media", joinColumns = @JoinColumn(name = "lead_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
	List<Media> medias;

	@OneToMany(mappedBy = "lead", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Invitation> invitations;

	// DEPRECATED
	private String oldid;
	private BigDecimal fee;
	private String type;
	@Enumerated(EnumType.STRING)
	private Vendor vendor;

	/*******************
	 * 
	 * METHODS
	 *
	 *******************/

	public enum Vendor {
		HOME_ADVISOR, NETWORX, PHONE_CALL, EMAIL, FORM_PROCESSOR
	}

	public void addMedia(Media media) {
		if (this.medias == null)
			this.medias = new ArrayList<Media>();
		if (!this.medias.contains(media))
			this.medias.add(media);
	}

	public void removeMedia(Media media) {
		if (this.medias != null) {
			if (this.medias.contains(media))
				this.medias.remove(media);
		}
	}

	public void addInvitation(Invitation invitation) {
		if (this.invitations == null)
			this.invitations = new ArrayList<Invitation>();
		if (!this.invitations.contains(invitation)) {
			this.invitations.add(invitation);
			if (!invitation.getLead().equals(this))
				invitation.setLead(this);
		}
	}

	public void removeInvitation(Invitation invitation) {
		if (this.invitations != null) {
			if (this.invitations.contains(invitation))
				this.invitations.remove(invitation);
			if (invitation.getLead().equals(this))
				invitation.setLead(null);
		}
	}

	public void addProposal(Proposal proposal) {
		if (this.proposals == null)
			this.proposals = new ArrayList<Proposal>();
		if (!this.proposals.contains(proposal)) {
			this.proposals.add(proposal);
			if (!proposal.getLead().equals(this))
				proposal.setLead(this);
		}
	}

	public void removeProposal(Proposal proposal) {
		if (this.proposals != null) {
			if (this.proposals.contains(proposal))
				this.proposals.remove(proposal);
			if (proposal.getLead().equals(this))
				proposal.setLead(null);
		}
	}

	public void setNote(String note) throws Exception {
		throw new Exception();
	}

	public void addNote(String note) {
		this.notes = note = new StringBuilder().append(Converter.dateToString(new Date())).append(System.lineSeparator()).append(note)
		    .append(System.lineSeparator()).append(System.lineSeparator()).append((!StringUtils.isEmpty(this.notes) ? this.notes : "")).toString();
	}

}
