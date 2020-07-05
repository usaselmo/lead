package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.allscontracting.model.EventLog;

public interface EventoLogRepository extends JpaRepository<EventLog, Long> {

	@Query("SELECT e FROM EventLog e WHERE e.objectName = ?1 AND e.objectId = ?2 ORDER BY e.eventTime DESC ") 
	List<EventLog> findEventLogs(String objectName, String objecId);

}
