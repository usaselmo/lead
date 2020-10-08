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
import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.model.Proposal;

public interface LeadRepository extends JpaRepository<Lead, Long> {

	@Query("SELECT count(l) FROM Lead l WHERE l.event IN ?1 ")
	Long countByEvent(List<Event> events);

	Page<Lead> findAllByEvent(Pageable pageable, Event eventType);

	@Query("SELECT p FROM Lead l, Proposal p WHERE p.id = ?1 ")
	List<Proposal> findProposals(Long leadId);

	@Query("SELECT l FROM Lead l WHERE l.event IN ?1 ")
	List<Lead> search(List<Event> events, Pageable pageable);

	@Query("SELECT l FROM Lead l WHERE l.event IN ?2 AND ( "
			+ " l.title LIKE %?1% OR l.description LIKE %?1% " 
			+ " OR ( l.client.id IN (SELECT p1.id FROM Person p1 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.contact.id IN (SELECT p2.id FROM Person p2 WHERE NAME LIKE %?1% OR ADDRESS LIKE %?1% OR PHONE LIKE %?1% OR EMAIL LIKE %?1% ) ) "
			+ " OR ( l.company.id IN ( SELECT c1.id FROM Company c1 WHERE NAME LIKE %?1% OR EMAIL LIKE %?1% OR ADDRESS LIKE %?1% ) ) "
			+ " )")
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
