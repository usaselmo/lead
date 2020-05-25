package com.allscontracting.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allscontracting.event.EventType;
import com.allscontracting.model.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, String> {

	long countByEvent(EventType event);
	
	Page<Lead> findAllByEvent(Pageable pageable, EventType eventType);

}
