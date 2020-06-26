package com.allscontracting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.service.UserService;

@RequestMapping("users")
@RestController
public class UserController {

	@Autowired UserService userService;
	
	@GetMapping("")
	public List<UserDTO> getUsersByName(@RequestParam String userName){
		return this.userService.findLikeName(userName);
	}
	
	@PutMapping("")
	public UserDTO updateUser(@RequestBody UserDTO user) {
		return this.userService.persist(user);
	}
	
}
