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
		try {
			User user = userRepo.findById(Long.valueOf(userDTO.getId())).orElseThrow(()->new LeadsException("User not found"));
			user.setEmail(userDTO.getEmail());
			user.setEnabled(userDTO.isEnabled());
			user.setName(userDTO.getName());
			user.setCompany(companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")));
			removeProfiles(user);
			addProfiles(userDTO, user);
			userRepo.flush();
			return UserDTO.of(userRepo.save(user));
		} catch (Exception e) {
			throw new LeadsException("Couldn't save User details.");
		}
	}

	private void removeProfiles(User user) {
		for (int i = 0; i < user.getProfiles().size(); i++) {
			if(user.getProfiles().get(i)!=null) { 
				user.removeUserProfile(user.getProfiles().get(i));
			}
		}
	}

	private void addProfiles(UserDTO userDTO, final User user) {
		userDTO.getProfiles().stream().forEach(p->{
			user.addUserProfile(userProfileRepo.save(UserProfile.builder().user(user).profile(UserProfile.Description.valueOf(p)).build()));
		});
	}

	public List<UserDTO> findEstimators() {
		return userRepo.findEstimators().stream().map(e->UserDTO.of(e)).collect(Collectors.toList());
	}
	
}
