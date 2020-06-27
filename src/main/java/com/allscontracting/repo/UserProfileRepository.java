package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
