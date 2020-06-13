package com.allscontracting.dto;

import java.text.NumberFormat;

import com.allscontracting.model.Proposal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProposalDTO {
	private String id;
	private String number;
	private String total;
	private String scopeOfWork;
	private boolean callMissUtility;
	private String paymentSchedule;
	private String workWarranty;
	private boolean emailed;
	private String note;
	
	public static final ProposalDTO proposalToDTO(Proposal proposal) {
		return ProposalDTO.builder()
				.id(String.valueOf(proposal.getId()))
				.number(String.valueOf(proposal.getNumber()))
				.total(NumberFormat.getCurrencyInstance().format(proposal.getTotal()))
				.callMissUtility(proposal.isCallMissUtility())
				.paymentSchedule(proposal.getPaymentSchedule())
				.workWarranty(proposal.getWorkWarranty())
				.emailed(proposal.isEmailed())
				.note(proposal.getNote())
				.build();
	}
}
