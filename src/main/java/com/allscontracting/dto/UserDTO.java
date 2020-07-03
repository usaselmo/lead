package com.allscontracting.dto;

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
  private List<UserProfileDTO> profiles;
  
	public static final UserDTO toDTO(User user ) {
		return UserDTO.builder()
				.company(CompanyDTO.companyToDTO(user.getCompany()))
				.email(user.getEmail())
				.enabled(user.isEnabled())
				.id(String.valueOf(user.getId()))
				.name(user.getName())
				.password("")
				.profiles(user.getProfiles().stream().map(p->UserProfileDTO.toDTO(p)).collect(Collectors.toList()))
				.build();
	}
}
