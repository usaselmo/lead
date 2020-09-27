package com.allscontracting.service;

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
	@Autowired PasswordEncoder passencoder;
	
	public List<UserDTO> findLikeName(String name){
		return this.userRepo.findLikeName(name).stream().map(u->UserDTO.of(u)).collect(Collectors.toList());
	}

	@Transactional
	public UserDTO update(UserDTO userDTO) throws LeadsException {
		try {
			User user = userRepo.findById(Long.valueOf(userDTO.getId())).orElseThrow(()->new LeadsException("User not found"));
			user.setEmail(userDTO.getEmail());
			user.setEnabled(userDTO.isEnabled());
			user.setName(userDTO.getName());
			user.setCompany(userDTO.getCompany()!=null?companyRepo.findById(userDTO.getCompany().getId()).orElseThrow(()->new LeadsException("Company not found")):null);
			removeProfiles(user);
			addProfiles(userDTO, user);
			userRepo.flush();
			return UserDTO.of(userRepo.save(user));
		} catch (Exception e) {
			throw new LeadsException("Couldn't save User details.");
		}
	}

	public UserDTO createUser(UserDTO userDTO) {
		try {
			User user = UserDTO.toUser(userDTO);
			user.setProfiles(null);
			user.setCompany(userDTO.getCompany()!=null?companyRepo.findById(userDTO.getCompany().getId()).orElse(null):null);
			user.setPassword(this.passencoder.encode(user.getPassword()));
			userRepo.save(user);
			addProfiles(userDTO, user);
			return UserDTO.of(userRepo.save(user)); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
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
