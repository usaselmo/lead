package com.allscontracting.service.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import com.allscontracting.exception.LeadsException;
import com.allscontracting.model.Invitation;
import com.allscontracting.service.Converter;

@Service
public class Mail_Service_DELETE_ME {

	/*
	 * public MailSender sendHiringDecisionEmail(MailDTO emailDTO) throws
	 * IOException { MailSender mail = new MailSender(
	 * Stream.of(emailDTO.getTo()).map(e->e.getName()).collect(Collectors.toList()).
	 * toArray(new String[0]),
	 * Stream.of(emailDTO.getBcc()).map(e->e.getName()).collect(Collectors.toList())
	 * .toArray(new String[0]), "Have you made a decision ?",
	 * getHiringDecisionText(emailDTO), null); return mail; }
	 */

	/*
	 * public MailSender sendCantReachEmail(MailDTO mailDTO) throws IOException {
	 * MailSender mail = new
	 * MailSender(Stream.of(mailDTO.getTo()).map(e->e.getName()).collect(Collectors.
	 * toList()).toArray(new String[0]),
	 * Stream.of(mailDTO.getBcc()).map(e->e.getName()).collect(Collectors.toList()).
	 * toArray(new String[0]), "We are trying to reach you for your job",
	 * getCantReachText(mailDTO), null); return mail; }
	 */

	/*
	 * public MailSender sendProposalByEmail(Proposal proposal, File
	 * proposalPdfFile, MailDTO emailDTO) throws IOException { MailSender mail = new
	 * MailSender(
	 * Stream.of(emailDTO.getTo()).map(e->e.getName()).collect(Collectors.toList()).
	 * toArray(new String[0]),
	 * Stream.of(emailDTO.getBcc()).map(e->e.getName()).collect(Collectors.toList())
	 * .toArray(new String[0]), "Your Proposal from All's Contracting",
	 * this.getProposalText(proposal, emailDTO), new File[] {proposalPdfFile} );
	 * return mail; }
	 */
	
	/*
	 * public MailSender sendInvitationToBid(Invitation invitation, File...
	 * attachments) throws LeadsException { MailSender mail = new MailSender(new
	 * String[] {invitation.getContact().getEmail()}, null,
	 * "REQUEST FOR PROPOSAL - " + invitation.getLead().getTitle(),
	 * this.getInvitationText(invitation), attachments); return mail; }
	 */

	/*
	 * private String getInvitationText(Invitation invitation) throws LeadsException
	 * { try { File file = getFile("templates/email/invitation-to-bid.html"); String
	 * string = new String(Files.readAllBytes(file.toPath())); string =
	 * string.replace("{companyName}", invitation.getCompany().getName()); string =
	 * string.replace("{companyAddress}",
	 * StringUtils.isNotBlank(invitation.getCompany().getAddress())?invitation.
	 * getCompany().getAddress():""); string = string.replace("{contactName}",
	 * invitation.getContact().getName()); string = string.replace("{contactPhone}",
	 * invitation.getContact().getPhone()); string =
	 * string.replace("{leadDescription}", invitation.getLead().getDescription());
	 * string = string.replace("{leadTitle}", invitation.getLead().getTitle());
	 * string = string.replace("{leadAddress}", invitation.getLead().getAddress());
	 * string = string.replace("{leadDueDate}",
	 * Converter.dateToString(invitation.getDueDate(), Converter.MM_dd_yyyy_hh_mm));
	 * return string; } catch (Exception e) { e.printStackTrace(); throw new
	 * LeadsException("Could not fill e-mail template."); } }
	 */

	/*
	 * private String getHiringDecisionText(MailDTO mailDTO) throws IOException {
	 * File file = getFile("templates/email/hiring-decision.html"); String string =
	 * new String(Files.readAllBytes(file.toPath())); string =
	 * string.replace("{person.name}", mailDTO.getTo()[0].getName() ); string =
	 * string.replace("{additionalText}", mailDTO.getText()); return string; }
	 */

	/*
	 * private String getCantReachText(MailDTO mailDTO) throws IOException { File
	 * file = getFile("templates/email/cantreach.html"); String string = new
	 * String(Files.readAllBytes(file.toPath())); string =
	 * string.replace("{person.name}", ( (Client) mailDTO.getTo()[0]).getName() );
	 * string = string.replace("{additionalText}", mailDTO.getText()); return
	 * string; }
	 */

	/*
	 * private String getProposalText(Proposal proposal, MailDTO mailDTO) throws
	 * IOException { File file = getFile("templates/email/proposal.html"); String
	 * string = new String(Files.readAllBytes(file.toPath())); string =
	 * string.replace("{person.name}", ( (Client) mailDTO.getTo()[0]).getName());
	 * string = string.replace("{number}", String.valueOf(proposal.getNumber()));
	 * string = string.replace("{additionalText}", mailDTO.getText()); return
	 * string; }
	 */

	/*
	 * private File getFile(String fileName) throws IOException { InputStream is =
	 * this.getClass().getClassLoader().getResourceAsStream(fileName); Path tempFile
	 * = Files.createTempFile("", ""); Files.copy(is, tempFile,
	 * StandardCopyOption.REPLACE_EXISTING); IOUtils.closeQuietly(is); return
	 * tempFile.toFile(); }
	 */

}
