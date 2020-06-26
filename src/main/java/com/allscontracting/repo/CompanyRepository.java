package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
