package com.allscontracting;

import static org.junit.Assert.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.allscontracting.dto.UserDTO;
import com.allscontracting.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {

	@Autowired
	UserService userService;

	@Test
	@Transactional
	public void testFindEstimators() throws Exception {
		List<UserDTO> res = this.userService.findEstimators();
		assertThat(res.size(), CoreMatchers.is(1));
	}

}
