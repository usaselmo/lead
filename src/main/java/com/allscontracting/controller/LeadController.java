package com.allscontracting.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allscontracting.dto.EventDTO;
import com.allscontracting.dto.FilterDTO;
import com.allscontracting.dto.InvitationDTO;
import com.allscontracting.dto.LeadDTO;
import com.allscontracting.dto.LeadEntity;
import com.allscontracting.dto.MailDTO;
import com.allscontracting.event.Event;
import com.allscontracting.exception.LeadsException;
import com.allscontracting.security.LeadUserDetails;
import com.allscontracting.service.InvitationService;
import com.allscontracting.service.LeadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("leads")
@RequiredArgsConstructor
public class LeadController {

	private static final String UNEXTECTED_ERROR = "Unexpected error.";
	private final LeadService leadService;
	private final InvitationService invitationService;

	@GetMapping
	public LeadEntity list(@RequestParam int pageRange, @RequestParam int lines, @RequestParam Event event, @RequestParam String text) throws Exception {
		try {
			LeadEntity res = this.leadService.list(new FilterDTO(pageRange, lines, text, EventDTO.of(event)));
			return res;
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PutMapping
	public LeadEntity update(@RequestBody LeadDTO leadDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().lead(leadService.update(leadDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build()
			    .addSuccessMessage("Lead updated.");
		} catch (NumberFormatException | LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PostMapping
	public LeadEntity save(@RequestBody LeadDTO leadDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().lead(leadService.save(leadDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser())).build()
			    .addSuccessMessage("Lead Created.");
		} catch (NumberFormatException | LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PostMapping(value = "{id}/fireevent")
	public LeadEntity fireEvent(@PathVariable String id, @RequestBody EventDTO event, @Autowired Authentication authentication) throws LeadsException {
		try {
			LeadDTO lead = leadService.fireEvent(id, Event.reverse(event.getName()), ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().lead(lead).build().addSuccessMessage("Event fired.");
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PostMapping(value = "{id}/addNote")
	public LeadEntity addNewNote(@PathVariable String id, @RequestBody String note) throws LeadsException {
		try {
			LeadDTO lead = leadService.addNewNote(id, note);
			return LeadEntity.builder().lead(lead).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PostMapping("{leadId}/invitations")
	public LeadEntity createInvitation(@PathVariable Long leadId, @RequestBody InvitationDTO invitationDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().invitation(invitationService.save(invitationDTO, leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser()))
			    .build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PutMapping("{leadId}/invitations")
	public LeadEntity updateInvitation(@PathVariable Long leadId, @RequestBody InvitationDTO invitationDTO, @Autowired Authentication authentication) {
		try {
			return LeadEntity.builder().invitation(invitationService.update(invitationDTO, leadId, ((LeadUserDetails) authentication.getPrincipal()).getUser()))
			    .build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@DeleteMapping("{leadId}/invitations/{invitationId}")
	public LeadEntity deleteInvitation(@PathVariable Long leadId, @PathVariable Long invitationId, @Autowired Authentication authentication) {
		try {
			invitationService.deleteInvitation(leadId, invitationId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().build().addSuccessMessage("Invitation deleted.");
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@GetMapping("{leadId}")
	public LeadEntity getLead(@PathVariable Long leadId) {
		try {
			LeadDTO leadDTO = this.leadService.findLead(leadId);
			return LeadEntity.builder().lead(leadDTO).build();
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PostMapping("/{leadId}/invitations/{invitationId}/email")
	public LeadEntity sendInvitationByEmail(@RequestBody InvitationDTO invitationDTO, MailDTO mailDTO, @Autowired Authentication authentication) {
		try {
			leadService.sendInvitationByEmail(invitationDTO, ((LeadUserDetails) authentication.getPrincipal()).getUser(), mailDTO);
			return LeadEntity.builder().build().addSuccessMessage("Invitation sent by e-mail.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@PutMapping("/{leadId}/invitations/{invitationId}/email")
	public LeadEntity markInvitationAsEmailed(@PathVariable Long invitationId, @Autowired Authentication authentication) {
		try {
			InvitationDTO invitationDTO = leadService.markAsEmailed(invitationId, ((LeadUserDetails) authentication.getPrincipal()).getUser());
			return LeadEntity.builder().invitation(invitationDTO).build().addSuccessMessage("Invitation sent by e-mail.");
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

	@GetMapping(value = "{leadId}/invitations/{invitationId}/proposals/{proposalId}/pdf")
	public LeadEntity getInvitationPdf(HttpServletResponse response, @PathVariable Long invitationId, @PathVariable Long proposalId) {
		try {
			this.leadService.getInvitationAsPdfStream(response, invitationId, proposalId);
			return LeadEntity.builder().build().addSuccessMessage("Invitation deleted.");
		} catch (LeadsException e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return LeadEntity.builder().build().addErrorMessage(UNEXTECTED_ERROR);
		}
	}

}
