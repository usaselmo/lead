package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.EmailMarketingCampain;

public interface EmailMarketingRepository extends JpaRepository<EmailMarketingCampain, Long> {

}
