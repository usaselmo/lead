package com.allscontracting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = 9167278336604403557L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Long id;
	
	@Column(nullable = false, unique = true) 
	private String email; 
	
	private String password;
	private String name; 
	private boolean enabled;
	
	@ManyToOne(fetch=FetchType.EAGER) private Company company;

  @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
  private List<UserProfile> profiles;
  
  public void addUserProfile(UserProfile userProfile) {
  	if(this.profiles==null)
  		this.profiles = new ArrayList<>(0);
  	if(!this.profiles.contains(userProfile)) 
  		this.profiles.add(userProfile);
  	userProfile.setUser(this);
  }
  
}
