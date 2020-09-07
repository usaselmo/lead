package com.allscontracting.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.model.UserProfile;
import com.allscontracting.service.UserService;

@RequestMapping("users")
@RestController
public class UserController {

	@Autowired UserService userService;
	
	@GetMapping("estimators")
	public List<UserDTO> getEstimators(){
		return userService.findEstimators();
	}
	
	@GetMapping("")
	public List<UserDTO> getUsersByName(@RequestParam String userName){
		return this.userService.findLikeName(userName);
	}
	
	@PostMapping("")
	public UserDTO createUser(@RequestBody UserDTO userDTO) throws Exception {
		return userService.createUser(userDTO);
	}
	
	@PutMapping("")
	public UserDTO updateUser(@RequestBody UserDTO userDTO) throws Exception {
		return userService.update(userDTO);
	}

	@GetMapping("profiles")
	public List<String> getProfiles(){
		return Stream.of(UserProfile.Description.values()).map(p->p.name()).collect(Collectors.toList());
	}
	
}
