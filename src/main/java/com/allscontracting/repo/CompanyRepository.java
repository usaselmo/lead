package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allscontracting.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	/*
	 * @Query("SELECT c FROM Company c WHERE c.name LIKE %?1% ORDER BY c.name ")
	 * List<Company> findLikeName(String companyName);
	 */

}
