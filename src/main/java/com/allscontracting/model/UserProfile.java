package com.allscontracting.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_profile")
public class UserProfile implements Serializable{

	private static final long serialVersionUID = -1113102765450097516L;
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	@ManyToOne
  private User user;

  @NotNull
	@Enumerated(EnumType.STRING) 
  private Description profile;
	
	public enum Description {
		GUEST, USER, ESTIMATOR, MANAGER, ADMIN;
	}
	
}
