package com.allscontracting.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.User;
import com.allscontracting.model.UserProfile;
import com.allscontracting.repo.CompanyRepository;
import com.allscontracting.repo.UserProfileRepository;
import com.allscontracting.repo.UserRepository;

@Service
public class UserService {

	@Autowired UserRepository userRepo;
	@Autowired CompanyRepository companyRepo;
	@Autowired UserProfileRepository userProfileRepo;
	
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

	@Transactional
	public UserDTO update(UserDTO userDTO) throws LeadsException {
		User user = userRepo.findById(Long.valueOf(userDTO.getId())).orElseThrow(()->new LeadsException("User not found"));
		user.setEmail(userDTO.getEmail());
		user.setEnabled(userDTO.isEnabled());
		user.setName(userDTO.getName());
		user.setCompany(companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")));
		return UserDTO.of(userRepo.save(user));
	}

	public UserDTO updateUserProfiles(UserDTO userDTO) throws NumberFormatException, LeadsException {
		User user = userRepo.findById(Long.valueOf(userDTO.getId())).orElseThrow(()->new LeadsException("User not found"));
		
		
		List<UserProfile> profiles = user.getProfiles();
		for (UserProfile userProfile : profiles) {
			userProfile.setUser(null);
		}
		user.setProfiles(null);

		
		userDTO.getProfiles().stream().forEach(p->{
			UserProfile profile = new UserProfile(null, user, UserProfile.Description.USER);
			user.addUserProfile(profile);
			profile.setUser(user);
			profile = userProfileRepo.save(profile);
		});
		return UserDTO.of(userRepo.save(user));
	}

	public List<UserDTO> findEstimators() {
		return userRepo.findEstimators().stream().map(e->UserDTO.of(e)).collect(Collectors.toList());
	}
	
}
