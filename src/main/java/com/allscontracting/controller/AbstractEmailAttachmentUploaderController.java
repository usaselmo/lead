package com.allscontracting.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.type.Attachment;
import com.allscontracting.type.Attachment.AttachmentId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
public abstract class AbstractEmailAttachmentUploaderController {

	private static final int CLEANUP_INTERVAL = 1000*60*15; //every 15 minutes
	private static final Map<AttachmentId, Attachment> FILES = new HashMap<AttachmentId, Attachment>();

	protected final List<File> getAttachments(String emailId) {
		List<Attachment> attachments = FILES.entrySet().stream().filter(f->f.getKey().getEmailId().equals(emailId)).map(Entry::getValue).collect(Collectors.toList());
		return attachments.stream().map(fu->fu.getFile()).collect(Collectors.toList());
	}	

	protected final void removeAttachments(String emailId) {
		FILES.entrySet().removeIf(f->f.getKey().getEmailId().equals(emailId));
	}

	/**
	 * Upload Email attachment - for sendCantReachEmail and sendHiringDecisionEmail methods
	 */
	@PostMapping("emailattachments/{emailId}")
	public final void uploadEmailAttachments(@PathVariable String emailId, @RequestParam("file") List<MultipartFile> multiPartFiles) throws IOException {
		for (MultipartFile multiPartFile : multiPartFiles) {
			File file = Files.write(Files.createTempFile("", multiPartFile.getOriginalFilename()), multiPartFile.getBytes()).toFile();
			file.deleteOnExit();
			FILES.put(new AttachmentId(emailId, multiPartFile.getOriginalFilename()), new Attachment(file, LocalDateTime.now()));
			log.info("Attachment uploaded: {} {}", emailId, multiPartFile.getOriginalFilename());
		}
	}

	/**
	 * Find email attachments list
	 */
	@GetMapping("emailattachments/{emailId}")
	public final List<String> findEmailAttachments(@PathVariable String emailId) throws Exception {
		return FILES.entrySet().stream().filter(f -> f.getKey().getEmailId().equals(emailId)).map(f -> f.getKey().getFileId()).collect(Collectors.toList());
	}

	/**
	 * Delete email attachment
	 */
	@DeleteMapping("emailattachments/{emailId}/{fileId}")
	public final void deleteEmailAttachment(@PathVariable String emailId, @PathVariable String fileId) throws Exception {
		FILES.entrySet().removeIf(f -> f.getKey().getEmailId().equals(emailId) && f.getKey().getFileId().equals(fileId));
		log.info("Attachment deleted: {} {}", emailId, fileId);
	}
	
	@Scheduled(fixedRate = CLEANUP_INTERVAL)
	private final void cleanUp(){
		log.info("cleanup method called...");
		FILES.entrySet().removeIf(f->f.getValue().getTime().isBefore(LocalDateTime.now().minus(CLEANUP_INTERVAL/1000, ChronoUnit.SECONDS)));
	}

}
