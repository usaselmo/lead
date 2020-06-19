package com.allscontracting.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.allscontracting.model.User;
import com.allscontracting.repo.UserRepository;

@Service
public class LeadDetailsService implements UserDetailsService {

	@Autowired UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findUserByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new LeadUserDetails(user);
	}

}
