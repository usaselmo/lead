package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByEmail(String email);
	
}
