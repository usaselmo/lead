package com.allscontracting.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProposalDTO {
	private String id;
	private String number;
	private String total;
	private String fileName;
	private String scopeOfWork;
	private boolean callMissUtility;
	private String paymentSchedule;
	private String workWarranty;
	private boolean emailed;
	private String note;
}
