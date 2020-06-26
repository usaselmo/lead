package com.allscontracting.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.model.User;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.UserRepository;

@Service
public class UserService {

	@Autowired UserRepository userRepo;
	@Autowired CompanyRepository companyRepo;
	
	public List<UserDTO> findLikeName(String name){
		return this.userRepo.findLikeName(name).stream().map(u->UserDTO.userToDTO(u)).collect(Collectors.toList());
	}

	public UserDTO persist(UserDTO userDTO) {
		if (userDTO.getId() != null)
			return this.update(userDTO);
		else
			return this.create(userDTO);
	}

	@Autowired PasswordEncoder passencoder;
	private UserDTO create(UserDTO userDTO) {
		User user = new User();
		user.setCompany(this.companyRepo.findOne(userDTO.getCompany().getId()));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setPassword(passencoder.encode(userDTO.getPassword()));
		user.setProfiles(Collections.emptyList());
		user = this.userRepo.save(user);
		
		
		
		return null;
	}

	private UserDTO update(UserDTO userDTO) {
		User user = userRepo.findOne(userDTO.getId());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setCompany(companyRepo.findOne(userDTO.getCompany().getId()));
		return UserDTO.userToDTO(userRepo.save(user));
	}
	
}
