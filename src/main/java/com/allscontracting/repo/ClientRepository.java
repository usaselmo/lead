package com.allscontracting.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.allscontracting.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query("SELECT c FROM Client c WHERE c.name LIKE %?1% ORDER BY c.name")
	List<Client> findLikeName(String name);

}
