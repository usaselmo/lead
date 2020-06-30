package com.allscontracting.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.exception.LeadsException;
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

	public UserDTO persist(UserDTO userDTO) throws LeadsException {
		if (userDTO.getId() != null)
			return this.update(userDTO);
		else
			return this.create(userDTO);
	}

	@Autowired PasswordEncoder passencoder;
	private UserDTO create(UserDTO userDTO) throws LeadsException {
		User user = new User();
		user.setCompany(this.companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setPassword(passencoder.encode(userDTO.getPassword()));
		user.setProfiles(Collections.emptyList());
		return UserDTO.userToDTO(this.userRepo.save(user));
	}

	private UserDTO update(UserDTO userDTO) throws LeadsException {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(()->new LeadsException("User not found"));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setCompany(companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("User not found")));
		return UserDTO.userToDTO(userRepo.save(user));
	}
	
}
