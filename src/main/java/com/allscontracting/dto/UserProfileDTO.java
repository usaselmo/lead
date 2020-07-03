package com.allscontracting.dto;

import com.allscontracting.model.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
	private Long id;
	private Long userId;
	private String profile;
	
	public final static UserProfileDTO toDTO(UserProfile userProfile) {
		return UserProfileDTO.builder()
		.id(userProfile.getId())
		.userId(userProfile.getUser().getId())
		.profile(userProfile.getProfile().toString())
		.build();
	}
}
