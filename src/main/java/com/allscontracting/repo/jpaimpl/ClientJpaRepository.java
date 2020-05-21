package com.allscontracting.repo.jpaimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.allscontracting.model.Client;

@Repository
public interface ClientJpaRepository extends JpaRepository<Client, Long> {

}
