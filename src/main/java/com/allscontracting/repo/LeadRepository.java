package com.allscontracting.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allscontracting.event.Event;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;
import com.allscontracting.model.Lead.Vendor;

public interface LeadRepository extends JpaRepository<Lead, Long>{
	
	long countByEvent(Event event);
	
	Page<Lead> findAllByEvent(Pageable pageable, Event eventType);
	
	@Query("SELECT p FROM Lead l, Proposal p WHERE p.id = ?1 ")
	List<Proposal> findProposals(Long leadId);
	
	@Query("SELECT l FROM Lead l WHERE l.event IN ?2 AND ( l.client.name LIKE %?1%  OR l.client.phone LIKE %?1% OR l.client.address LIKE %?1% OR l.client.email LIKE %?1% ) ")
	List<Lead> search(String text, List<Event> events, Pageable pageable);

	@Query("SELECT DISTINCT l.type FROM Lead l ORDER BY l.type")
	List<String> findByType();
	
	@Query("SELECT l FROM Lead l WHERE l.oldid = ?1 ")
	Optional<Lead> findByOldId(String oldId);
	
	default boolean existsByOldId(String oldId) {
		return findByOldId(oldId).isPresent();
	}
	
	@Query("SELECT l FROM Lead l WHERE l.vendor = ?1 AND l.date = ?2 AND l.description = ?3 AND l.client.name = ?4 AND l.oldid = ?5")
	List<Lead> findByVDD(Vendor vendor, Date date, String description, String clientName, String oldid);
	
}
