package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.model.Person;
import com.allscontracting.model.Proposal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProposalDTO {
	private String id;
	private String number;
	private String total;
	private String formattedTotal;
	private String scopeOfWork;
	private boolean callMissUtility;
	private String paymentSchedule;
	private String workWarranty;
	private boolean emailed;
	private String note;
	private List<ItemDTO> items;
	private Person estimator;
	
	public static final ProposalDTO of(Proposal proposal) {
		if(proposal==null)
			return ProposalDTO.builder().build();
		return ProposalDTO.builder()
				.id(String.valueOf(proposal.getId()))
				.number(String.valueOf(proposal.getNumber()))
				.total(proposal.getTotal().toString().replaceAll("$",	""))
				.formattedTotal(NumberFormat.getCurrencyInstance().format(proposal.getTotal()))
				.scopeOfWork(proposal.getScopeOfWork())
				.callMissUtility(proposal.isCallMissUtility())
				.paymentSchedule(proposal.getPaymentSchedule())
				.workWarranty(proposal.getWorkWarranty())
				.emailed(proposal.isEmailed())
				.note(proposal.getNote())
				.items(proposal.getItems().stream().map(i->ItemDTO.of(i)).collect(Collectors.toList()))
				.estimator(proposal.getEstimator())
				.build();
	}
	
	public static final Proposal toProposal(ProposalDTO proposalDTO) {
		Proposal proposal = new Proposal();
		proposal.setId(StringUtils.isBlank(proposalDTO.getId())?null:Long.valueOf(proposalDTO.getId()));
		proposal.setCallMissUtility(proposalDTO.isCallMissUtility());
		proposal.setEmailed(proposalDTO.isEmailed());
		proposal.setNote(proposalDTO.getNote()); 
		proposal.setNumber(StringUtils.isBlank(proposalDTO.getNumber())?null:Long.valueOf(proposalDTO.getNumber()));
		proposal.setTotal(StringUtils.isBlank(proposalDTO.getTotal())?BigDecimal.ZERO:new BigDecimal(proposalDTO.getTotal().replace("$", "")));
		proposal.setPaymentSchedule(proposalDTO.getPaymentSchedule());
		proposal.setScopeOfWork(proposalDTO.getScopeOfWork());
		proposal.setTotal(StringUtils.isBlank(proposalDTO.getTotal())?BigDecimal.ZERO:new BigDecimal(proposalDTO.getTotal().replace("$", "").replace(",", "")));
		proposal.setWorkWarranty(proposalDTO.getWorkWarranty());
		proposal.setItems(proposalDTO.getItems().stream().map(i->ItemDTO.toItem(i)).collect(Collectors.toList()));
		proposal.setEstimator(proposalDTO.getEstimator());
		return proposal;
	}
	
}
