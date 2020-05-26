package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long>{
	
}
