package com.allscontracting.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.allscontracting.model.User;
import com.allscontracting.model.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDTO {
	private final String id;
	private final String email; 
	private final String password;
	private final String name; 
	private final boolean enabled;
	private final CompanyDTO company;
  private final List<String> profiles;
  
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
		u.setId(StringUtils.isEmpty(ud.getId())?null:Long.valueOf(ud.getId()));
		u.setName(ud.getName());
		u.setPassword(ud.getPassword());
		u.setProfiles(ud.getProfiles().stream().map(p->UserProfile.builder().user(u).profile(UserProfile.Description.valueOf(p)).build()).collect(Collectors.toList()));
		return u;
	}

}
