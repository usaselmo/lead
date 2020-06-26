package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allscontracting.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.name LIKE %?1% ORDER BY u.name")
	List<User> findLikeName(String name);
	
}
