package com.allscontracting.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeadDTO {
	private String id;
	private String vendor;
	private String date;
	private String description;
	private String fee;
	private String type;
	private String notes;
	private ClientDTO client;
	private List<ProposalDTO> proposals;
	private String event;//EventType
	private String visit;
}
