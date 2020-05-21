package com.allscontracting.repo;

import java.util.List;
import java.util.Optional;

public interface Repository<E, ID> {
	
	<T extends E> T save(T entity);
	
	Optional<E> findById(ID primaryKey);

	List<E> findAll();
	
	long count(); 
	
	void delete(E entity);

	boolean existsById(ID primaryKey);

}
