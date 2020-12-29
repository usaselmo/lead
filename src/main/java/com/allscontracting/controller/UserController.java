package com.allscontracting.controller;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.UserDTO;
import com.allscontracting.model.UserProfile;
import com.allscontracting.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("users")
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("estimators")
	public LeadEntity getEstimators() {
		return LeadEntity.builder().users(userService.findEstimators()).build();
	}

	@GetMapping("")
	public LeadEntity getUsersByName(@RequestParam String userName) {
		return LeadEntity.builder().users(this.userService.findLikeName(userName)).build();
	}

	@PostMapping("")
	public LeadEntity createUser(@RequestBody UserDTO userDTO) throws Exception {
		return LeadEntity.builder().user(userService.createUser(userDTO)).build();
	}

	@PutMapping("")
	public LeadEntity updateUser(@RequestBody UserDTO userDTO) throws Exception {
		return LeadEntity.builder().user(userService.update(userDTO)).build();
	}

	@GetMapping("profiles")
	public LeadEntity getProfiles() {
		return LeadEntity.builder().profiles(Stream.of(UserProfile.Description.values()).map(p -> p.name()).collect(Collectors.toList())).build();
	}

}
