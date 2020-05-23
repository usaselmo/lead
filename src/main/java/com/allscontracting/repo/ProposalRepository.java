package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Proposal;

public interface ProposalRepository extends JpaRepository<Proposal, Long>{

}
