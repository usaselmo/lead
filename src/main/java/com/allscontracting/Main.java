package com.allscontracting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.User;
import com.allscontracting.model.UserProfile;
import com.allscontracting.repo.UserProfileRepository;
import com.allscontracting.repo.UserRepository;

@SpringBootApplication
public class Main implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Main.class, arguments); 
	}

	
	@Override
	public void run(String... args) throws Exception {
		//removeProfiles(); 
		//addProfiles();
	}

	@Autowired UserRepository userRepo;
	@Autowired UserProfileRepository profileRepo;  

	public void addProfiles() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		user.addUserProfile(profileRepo.save(UserProfile.builder().user(user).profile(UserProfile.Description.ADMIN).build()));
		user.addUserProfile(profileRepo.save(UserProfile.builder().user(user).profile(UserProfile.Description.ESTIMATOR).build()));
		user = userRepo.save(user);
	}
	
	public void removeProfiles() throws Exception { 
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		for (int i = 0; i < user.getProfiles().size(); i++) {
			if(user.getProfiles().get(i)!=null) { 
				UserProfile p = user.getProfiles().get(i); 
				user.removeUserProfile(p);
			}
		}
		user.setProfiles(profileRepo.saveAll(user.getProfiles()));
		user = userRepo.save(user);	
	}
	
	public void addAndRemoveProfile() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		int previousSize = user.getProfiles().size();
		user.removeUserProfile(user.getProfiles().get(0));
		user.addUserProfile(UserProfile.builder().user(user).profile(UserProfile.Description.ADMIN).build());
		user = userRepo.save(user);
	}

}
