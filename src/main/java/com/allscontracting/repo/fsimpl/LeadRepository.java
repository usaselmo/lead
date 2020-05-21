package com.allscontracting.repo.fsimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Lead;
import com.allscontracting.tradutor.Translater;
import com.allscontracting.tradutor.TranslaterDispatcher;
import com.allscontracting.tradutor.impl.LeadHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LeadRepository extends AbstractGenericFSRepository<Lead, String> { 

	@Autowired	TranslaterDispatcher translaterDispatcher;
	public static final Path PROPOSALS_FOLDER = Paths.get("C:\\Users\\Anselmo.asr\\Google Drive\\All's Contracting\\proposals");
	
	@Override
	public Path getPersistenceFile() {
		return Paths.get("C:\\Users\\Anselmo.asr\\Google Drive\\All's Contracting\\app\\leads.txt");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <S extends Lead> S save(S entity) {
		if(!this.existsById(entity.getId())) {
			try {
				Translater<Lead> translater = (Translater<Lead>) this.translaterDispatcher.dispatch(entity.getVendor());
				String line = translater.entityToLocalFSFileLine(entity);
				saveToFile(line);
			} catch (LeadsException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	@Override
	public Optional<Lead> findById(String id) {
		try {
			return Files.readAllLines(getPersistenceFile()).stream()
					.filter(line -> LeadHelper.localFSFileLineToEntity(line, Lead.class).getId().equalsIgnoreCase(id))
					.map(line -> LeadHelper.localFSFileLineToEntity(line, Lead.class)).findFirst();
		} catch (IOException e) {
			log.error(e.getMessage());
			return Optional.empty();
		}	
	}

	@Override
	public List<Lead> findAll(){
		try {
			return Files.readAllLines(getPersistenceFile())
					.stream()
					.map(line -> LeadHelper.localFSFileLineToEntity(line, Lead.class))
					.sorted( (a,b)-> b.getDate().compareTo(a.getDate()) )
					.collect(Collectors.toList());
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

}
