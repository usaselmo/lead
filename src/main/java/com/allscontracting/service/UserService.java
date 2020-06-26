package com.allscontracting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

	public UserDTO update(UserDTO userDTO) {
		User user = userRepo.findOne(userDTO.getId());
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setCompany(companyRepo.findOne(userDTO.getCompany().getId()));
		return UserDTO.userToDTO(userRepo.save(user));
	}
	
}
