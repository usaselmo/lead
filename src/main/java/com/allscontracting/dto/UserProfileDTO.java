package com.allscontracting.dto;

import com.allscontracting.model.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileDTO {
	private final Long id;
	private final Long userId;
	private final String profile;
	
	public final static UserProfileDTO of(UserProfile userProfile) {
		if(userProfile==null)
			return null;
		return UserProfileDTO.builder()
		.id(userProfile.getId())
		.userId(userProfile.getUser().getId())
		.profile(userProfile.getProfile().toString())
		.build();
	}
}
