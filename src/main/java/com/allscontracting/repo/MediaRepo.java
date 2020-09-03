package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Media;

public interface MediaRepo extends JpaRepository<Media, Long> {

}
