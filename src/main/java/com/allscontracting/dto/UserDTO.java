package com.allscontracting.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.User;

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
			return UserDTO.builder().build();
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
		User u = new User();
		u.setCompany(CompanyDTO.toCompany(ud.getCompany()));
		return u;
	}

}
