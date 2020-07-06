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
import com.allscontracting.model.UserProfile;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.UserRepository;

@Service
public class UserService {

	@Autowired UserRepository userRepo;
	@Autowired CompanyRepository companyRepo;
	
	public List<UserDTO> findLikeName(String name){
		return this.userRepo.findLikeName(name).stream().map(u->UserDTO.of(u)).collect(Collectors.toList());
	}

	@Autowired PasswordEncoder passencoder;
	public UserDTO create(UserDTO userDTO) throws LeadsException {
		User user = new User();
		user.setCompany(this.companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setPassword(passencoder.encode(userDTO.getPassword()));
		user.setProfiles(Collections.emptyList());
		return UserDTO.of(this.userRepo.save(user));
	}

	public UserDTO update(UserDTO userDTO) throws LeadsException {
		User user = userRepo.findById(Long.valueOf(userDTO.getId())).orElseThrow(()->new LeadsException("User not found"));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setCompany(companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")));
		user.setProfiles(null);
		//userRepo.save(user);
		
		List<UserProfile> newProfiles = userDTO.getProfiles().stream().map(userDtoProfile->UserProfile.builder().profile(UserProfile.Description.valueOf(userDtoProfile)).build()).collect(Collectors.toList());
		newProfiles.stream().forEach(p->user.addUserProfile(p));
		//user.setProfiles(newProfiles);

		User u = userRepo.save(user);
		return UserDTO.of(u);
	}

	public List<UserDTO> findEstimators() {
		return userRepo.findEstimators().stream().map(e->UserDTO.of(e)).collect(Collectors.toList());
	}
	
}
