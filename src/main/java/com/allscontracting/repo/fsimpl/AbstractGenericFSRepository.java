package com.allscontracting.repo.fsimpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.repo.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGenericFSRepository<E, ID> implements Repository<E, ID> {

	abstract Path getPersistenceFile();

	@Override
	public boolean existsById(ID id) {
		return this.findById(id).isPresent();
	}

	@Override
	public long count() {
		return Stream.of(this.findAll()).count();
	}

	@Override
	public void delete(E entity) {
		// TODO Auto-generated method stub
		log.error("delete method not implemented yet");
	}

	protected void saveToFile(String dataToWriteToFile) throws LeadsException {
		File leadFile = getPersistenceFile().toFile();
		if (!leadFile.exists()) {
			createFile(leadFile);
		}
		try {
			FileWriter crunchifyWriter;
			crunchifyWriter = new FileWriter(leadFile.getAbsoluteFile(), true);
			BufferedWriter bufferWriter = new BufferedWriter(crunchifyWriter);
			bufferWriter.write(dataToWriteToFile.toString());
			bufferWriter.close();
		} catch (Exception e) {
			throw new LeadsException("Could not save data to Leads File");
		}
	}

	private void createFile(File crunchifyFile) throws LeadsException {
		try {
			File directory = new File(crunchifyFile.getParent());
			if (!directory.exists()) {
				directory.mkdirs();
			}
			crunchifyFile.createNewFile();
		} catch (Exception e) {
			throw new LeadsException("Could not create File: " + crunchifyFile);
		}
	}
	
}
