package com.allscontracting;

import static org.junit.Assert.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.User;
import com.allscontracting.model.UserProfile;
import com.allscontracting.repo.UserProfileRepository;
import com.allscontracting.repo.UserRepository;
import com.allscontracting.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {

	@Autowired UserService userService;
	@Autowired UserRepository userRepo;
	@Autowired UserProfileRepository userProfileRepo;
	
	@Test
	public void testUserProfile_include() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		int previousSize = user.getProfiles().size();
		user.addUserProfile(UserProfile.builder().user(user).profile(UserProfile.Description.ADMIN).build());
		user = userRepo.save(user);
		assertThat(user.getProfiles().size() >= previousSize, CoreMatchers.is(true));
	}
	
	@Test
	public void testUserProfile_remove() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		int previousSize = user.getProfiles().size();
		user.removeUserProfile(user.getProfiles().get(0));
		user = userRepo.save(user);
		assertThat(user.getProfiles().size()<previousSize, CoreMatchers.is(true));
	}
	
	@Test
	public void testUserProfile_addAndRemove() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		int previousSize = user.getProfiles().size();
		user.removeUserProfile(user.getProfiles().get(0));
		user.addUserProfile(UserProfile.builder().user(user).profile(UserProfile.Description.ADMIN).build());
		user = userRepo.save(user);
		assertThat(user.getProfiles().size() <= previousSize, CoreMatchers.is(true));
	}

	@Test
	@Transactional
	@Ignore
	public void testFindEstimators() throws Exception {
		List<UserDTO> res = this.userService.findEstimators();
		assertThat(res.size(), CoreMatchers.is(1));
	}

}
