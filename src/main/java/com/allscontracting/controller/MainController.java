package com.allscontracting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allscontracting.service.FileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final FileService fileService;

	@GetMapping(path = { "/", "" })
	public String leadList() {
		return "lead/lead";
	}

	@GetMapping("/main/users")
	public String users() {
		return "user/user";
	}

	@GetMapping("/main/companies")
	public String companies() {
		return "company/company";
	}

	@GetMapping("/main/clients")
	public String clients() {
		return "client/client";
	}

	@PostMapping("/main/leads/{leadId}/file-upload")
	public String handleFileUpload(@PathVariable Long leadId, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		fileService.storeLeadMedia(file, leadId);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return null;
	}

}
