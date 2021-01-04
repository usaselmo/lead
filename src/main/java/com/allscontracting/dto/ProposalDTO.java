package com.allscontracting.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.allscontracting.model.Person;
import com.allscontracting.model.Proposal;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProposalDTO {
	private final String id;
	private final String number;
	private final String total;
	private final String formattedTotal;
	private final String scopeOfWork;
	private final boolean callMissUtility;
	private final String paymentSchedule;
	private final String workWarranty;
	private final boolean emailed;
	private final String note;
	private final List<ItemDTO> items;
	private final Person estimator;
	private final String date;
	private final boolean changeorder;
	private final boolean accepted;
	
	public static final ProposalDTO of(Proposal proposal) {
		if(proposal==null)
			return null;
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
				.date(Converter.dateToString(proposal.getDate(), Converter.MM_dd_yyyy))
				.changeorder(proposal.isChangeorder())
				.accepted(proposal.isAccepted())
				.build();
	}
	
	public static final Proposal toProposal(ProposalDTO proposalDTO) {
		if(proposalDTO==null)
			return null;
		Proposal proposal = new Proposal();
		proposal.setId(StringUtils.isEmpty(proposalDTO.getId())?null:Long.valueOf(proposalDTO.getId()));
		proposal.setCallMissUtility(proposalDTO.isCallMissUtility());
		proposal.setEmailed(proposalDTO.isEmailed());
		proposal.setNote(proposalDTO.getNote()); 
		proposal.setNumber(StringUtils.isEmpty(proposalDTO.getNumber())?null:Long.valueOf(proposalDTO.getNumber()));
		proposal.setTotal(StringUtils.isEmpty(proposalDTO.getTotal())?BigDecimal.ZERO:new BigDecimal(proposalDTO.getTotal().replace("$", "")));
		proposal.setPaymentSchedule(proposalDTO.getPaymentSchedule());
		proposal.setScopeOfWork(proposalDTO.getScopeOfWork());
		proposal.setTotal(StringUtils.isEmpty(proposalDTO.getTotal())?BigDecimal.ZERO:new BigDecimal(proposalDTO.getTotal().replace("$", "").replace(",", "")));
		proposal.setWorkWarranty(proposalDTO.getWorkWarranty());
		proposal.setItems(proposalDTO.getItems().stream().map(i->ItemDTO.toItem(i)).collect(Collectors.toList()));
		proposal.setDate(Converter.stringToDate(proposalDTO.getDate(), Converter.MM_dd_yy));
		proposal.setChangeorder(proposalDTO.isChangeorder());
		proposal.setAccepted(proposalDTO.isAccepted());
		return proposal;
	}
	
}
