package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allscontracting.event.Event;
import com.allscontracting.model.Lead;
import com.allscontracting.model.Proposal;

public interface LeadRepository extends JpaRepository<Lead, Long> {

	@Query("SELECT count(l) FROM Lead l WHERE l.event IN ?1 ")
	Long countByEvent(List<Event> events);

	@Query("SELECT p FROM Lead l, Proposal p WHERE p.id = ?1 ")
	List<Proposal> findProposals(Long leadId);
	
	@Query("SELECT l FROM Lead l WHERE l.event IN ?1 ")
	List<Lead> search(List<Event> events, Pageable pageable);

	@Query("SELECT count(l) FROM Lead l WHERE l.event IN ?1 ")
	long searchTotal(List<Event> events);
	
	@Query("SELECT l FROM Lead l WHERE l.event IN ?2 AND ( "
			+ " l.title LIKE %?1% OR l.description LIKE %?1% "
			+ " OR l.id LIKE %?1% "
			+ " OR l.description LIKE %?1% " 
			+ " OR ( l.client.id IN (SELECT p1.id FROM Person p1 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.contact.id IN (SELECT p2.id FROM Person p2 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.company.id IN ( SELECT c1.id FROM Company c1 WHERE NAME LIKE %?1% OR EMAIL LIKE %?1% OR ADDRESS LIKE %?1% ) ) "
			+ " )")
	List<Lead> search(String text, List<Event> events, Pageable pageable);

	@Query("SELECT count(l) FROM Lead l WHERE l.event IN ?2 AND ( "
			+ " l.title LIKE %?1% OR l.description LIKE %?1% "
			+ " OR l.id LIKE %?1% "
			+ " OR l.description LIKE %?1% " 
			+ " OR ( l.client.id IN (SELECT p1.id FROM Person p1 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.contact.id IN (SELECT p2.id FROM Person p2 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.company.id IN ( SELECT c1.id FROM Company c1 WHERE NAME LIKE %?1% OR EMAIL LIKE %?1% OR ADDRESS LIKE %?1% ) ) "
			+ " )")
	long searchTotal(String text, List<Event> events);

	@Query("SELECT DISTINCT l.type FROM Lead l ORDER BY l.type")
	List<String> findByType();

}
