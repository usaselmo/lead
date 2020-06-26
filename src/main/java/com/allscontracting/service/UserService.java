package com.allscontracting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allscontracting.model.User;
import com.allscontracting.repo.UserRepository;

@Service
public class UserService {

	@Autowired UserRepository userRepo;
	
	public List<User> findLikeName(String name){
		return this.userRepo.findLikeName(name);
	}
	
}
