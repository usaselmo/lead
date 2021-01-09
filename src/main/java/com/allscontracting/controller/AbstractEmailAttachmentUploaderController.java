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

import com.allscontracting.type.FileUploaded;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
public abstract class AbstractEmailAttachmentUploaderController {

	private static final int CLEANUP_INTERVAL = 1000*60*5; //every five minutes
	private static final Map<String, FileUploaded> FILES = new HashMap<String, FileUploaded>();

	protected final List<File> getAttachments(String key) {
		List<FileUploaded> attachments = FILES.entrySet().stream().filter(f->f.getKey().startsWith(key)).map(Entry::getValue).collect(Collectors.toList());
		return attachments.stream().map(fu->fu.getFile()).collect(Collectors.toList());
	}	

	protected final void removeAttachments(String key) {
		FILES.entrySet().removeIf(f->f.getKey().startsWith(key));
	}

	/**
	 * Upload Email attachment - for sendCantReachEmail and sendHiringDecisionEmail
	 * methods
	 */
	@PostMapping("{personId}/{leadId}/emailattachment/upload")
	public final void uploadEmailAttachments(@PathVariable String personId, @PathVariable String leadId, @RequestParam("file") MultipartFile file) throws IOException {
		File f = Files.write(Files.createTempFile("", file.getOriginalFilename()), file.getBytes()).toFile();
		f.deleteOnExit();
		FILES.put(personId + leadId + file.getOriginalFilename(), new FileUploaded(f, LocalDateTime.now()));
		log.info("Attachment uploaded: " + file.getOriginalFilename());
	}

	/**
	 * Find email attachments list
	 */
	@GetMapping("{personId}/leads/{leadId}/emailattachments")
	public final List<String> findEmailAttachments(@PathVariable String personId, @PathVariable String leadId) throws Exception {
		return FILES.entrySet().stream().filter(f -> f.getKey().startsWith(personId + leadId)).map(f -> f.getValue().getFile().getName()).collect(Collectors.toList());
	}

	/**
	 * Delete email attachment
	 */
	@DeleteMapping("{personId}/leads/{leadId}/emailattachments/{fileName}")
	public final void deleteEmailAttachment(@PathVariable String personId, @PathVariable String leadId, @PathVariable String fileName) throws Exception {
		FILES.entrySet().removeIf(f -> f.getKey().startsWith(personId + leadId) && f.getValue().getFile().getName().equals(fileName));
		log.info("Attachment deleted: " + fileName);
	}
	
	@Scheduled(fixedRate = CLEANUP_INTERVAL)
	private final void cleanUp(){
		FILES.entrySet().removeIf(f->f.getValue().getTime().isBefore(LocalDateTime.now().minus(5, ChronoUnit.MINUTES)));
	}

}
