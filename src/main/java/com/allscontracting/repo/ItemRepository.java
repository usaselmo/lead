package com.allscontracting.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.allscontracting.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

	
	
}
