package com.allscontracting.repo.fsimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.allscontracting.model.Proposal;

public class ProposalRepository extends AbstractGenericFSRepository<Proposal, Long>{

	@Override
	Path getPersistenceFile() {
		return Paths.get("C:\\Users\\Anselmo.asr\\Google Drive\\All's Contracting\\app\\proposal.txt");
	}

	@Override
	public <T extends Proposal> T save(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Proposal> findById(Long primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Proposal> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
