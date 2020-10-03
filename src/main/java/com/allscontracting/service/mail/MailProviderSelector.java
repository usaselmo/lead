package com.allscontracting.service.mail;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class MailProviderSelector {

	private final CantReachMailProvider cantReachMailProvider;
	private final HiringDecisionMailProvider hiringDecisionMailProvider;
	private final ProposalMailProvider proposalMailProvider;
	private final InvitationToBidMailProvider invitationToBidMailProvider;

	public MailProvider get(Mail.TYPE type) {
		switch (type) {
		case CANT_REACH:
			return this.cantReachMailProvider;
		case HIRING_DECISION:
			return this.hiringDecisionMailProvider;
		case PROPOSAL:
			return this.proposalMailProvider;
		case INVITATION_TO_BID:
			return this.invitationToBidMailProvider;
		default:
			return null;
		}
	}

}
