package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.allscontracting.event.EventType;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;

@Repository
public interface LeadRepository extends JpaRepository<Lead, String> {

	long countByEvent(EventType event);
	
	Page<Lead> findAllByEvent(Pageable pageable, EventType eventType);
	
	@Query("SELECT p FROM Lead l, Proposal p WHERE p.id = ?1 ")
	List<Proposal> findProposals(String leadId);
	
	@Query("SELECT l FROM Lead l WHERE l.client.name LIKE %?1%  OR l.client.phone LIKE %?1% OR l.client.address LIKE %?1% OR l.client.email LIKE %?1% ")
	List<Lead> search(String text, Pageable pageable);

}
