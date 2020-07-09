package com.allscontracting;

import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.assertj.core.util.Arrays;
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
	public void testUserProfile_01() throws Exception {
		User user = userRepo.findById(1L).orElseThrow(()->new LeadsException("User not found"));
		List<UserProfile> profiles = user.getProfiles();
		for (UserProfile userProfile : profiles) {
			userProfile.setUser(null);
			//user.getProfiles().remove(userProfile);
		}
		user.setProfiles(null);
		User u = userRepo.save(user);
		System.out.println(u);
	}

	@Test
	@Transactional
	@Ignore
	public void testFindEstimators() throws Exception {
		List<UserDTO> res = this.userService.findEstimators();
		assertThat(res.size(), CoreMatchers.is(1));
	}

}
