package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allscontracting.model.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, String> {

}
