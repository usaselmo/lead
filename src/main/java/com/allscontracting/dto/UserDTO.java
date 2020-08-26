package com.allscontracting.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.model.User;
import com.allscontracting.model.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private String id;
	private String email; 
	private String password;
	private String name; 
	private boolean enabled;
	private CompanyDTO company;
  private List<String> profiles;
  
	public static final UserDTO of(User user ) {
		if(user==null)
			return null;
		return UserDTO.builder()
				.company(CompanyDTO.of(user.getCompany()))
				.email(user.getEmail())
				.enabled(user.isEnabled())
				.id(String.valueOf(user.getId()))
				.name(user.getName())
				.password("")
				.profiles(user.getProfiles()==null||user.getProfiles().isEmpty()?Collections.emptyList():user.getProfiles().stream().map(p->p.getProfile().toString()).collect(Collectors.toList()))
				.build();
	}

	public static final User toUser(UserDTO ud) {
		if(ud==null)
			return null;
		User u = new User();
		u.setCompany(CompanyDTO.toCompany(ud.getCompany()));
		u.setEmail(ud.getEmail());
		u.setEnabled(ud.isEnabled());
		u.setId(StringUtils.isBlank(ud.getId())?null:Long.valueOf(ud.getId()));
		u.setName(ud.getName());
		u.setPassword(ud.getPassword());
		u.setProfiles(ud.getProfiles().stream().map(p->UserProfile.builder().user(u).profile(UserProfile.Description.valueOf(p)).build()).collect(Collectors.toList()));
		return u;
	}

}
