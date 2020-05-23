package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Proposal;

public interface ProposalJpaRepository extends JpaRepository<Proposal, Long>{

}
