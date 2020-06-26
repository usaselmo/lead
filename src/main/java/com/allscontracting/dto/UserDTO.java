package com.allscontracting.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDTO {
	private Long id;
	private String email; 
	private String password;
	private String name; 
	private boolean enabled;
	private CompanyDTO company;
  private List<String> profiles;
  
	public static final UserDTO userToDTO(User u ) {
		return UserDTO.builder()
				.company(CompanyDTO.companyToDTO(u.getCompany()))
				.email(u.getEmail())
				.enabled(u.isEnabled())
				.id(u.getId())
				.name(u.getName())
				.password("")
				.profiles(u.getProfiles().stream().map(p->p.getProfile().getDescription()).collect(Collectors.toList()))
				.build();
	}
}
