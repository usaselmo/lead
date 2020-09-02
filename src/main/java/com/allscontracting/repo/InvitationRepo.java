package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Invitation;

public interface InvitationRepo extends JpaRepository<Invitation, Long> {

}
