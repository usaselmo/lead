package com.allscontracting.dto;

import java.util.List;

import lombok.Builder;

@Builder
public class ProposalDTOFactory {

	public static ProposalDTO proposalDTO;

	public static List<ItemDTO> getItems() {
		return proposalDTO.getItems();
	}

}
