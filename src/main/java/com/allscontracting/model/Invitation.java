package com.allscontracting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@javax.persistence.Entity
@Table
public class Invitation implements Serializable {

	private static final long serialVersionUID = 5702457065775817600L;
	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
	private Long emailed;
	
	@Temporal(TemporalType.TIMESTAMP)	
	private Date date;
	
	@Temporal(TemporalType.TIMESTAMP)	
	private Date dueDate;
	
	@ManyToOne	
	private Company company;

	@OneToMany
	@JoinTable(name = "invitation_media", joinColumns = @JoinColumn(name = "invitation_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
	private List<Media> medias;

  @ManyToOne  @JoinColumn(name = "lead_id", insertable = true, updatable = false, nullable = false)	
  private Lead lead;

  @ManyToOne(fetch = FetchType.EAGER)
  private Person contact;
  
	public void addMedia(Media media) {
		if(this.medias == null)
			this.medias = new ArrayList<Media>();
		if(!this.medias.contains(media)) {
			this.medias.add(media);
		}
	}

	public void removeMedia(Media media) {
		if(this.medias != null) {
			if(this.medias.contains(media))
				this.medias.remove(media);
		}
	}
	
	
}
