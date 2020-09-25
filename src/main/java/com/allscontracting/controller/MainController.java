package com.allscontracting.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.allscontracting.service.FileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final FileService fileService;

	@GetMapping(path = { "/", "" })
	public String leadList(Model model) {
		model.addAttribute("target", "lead");
		return "index";
	}

	@GetMapping("/main/users")
	public String users(Model model) {
		model.addAttribute("target", "user");
		return "index";
	}

	@GetMapping("/main/companies")
	public String companies(Model model) {
		model.addAttribute("target", "company");
		return "index";
	}

	@GetMapping("/main/clients")
	public String clients(Model model) {
		model.addAttribute("target", "client");
		return "index";
	}

	@PostMapping("/main/leads/{leadId}/file-upload")
	@ResponseBody
	public void handleFileUpload(HttpServletResponse response,@PathVariable Long leadId, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		fileService.storeLeadMedia(file, leadId);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
	}
	
	@PostMapping("/main/leads/{leadId}/invitations/{invitationId}")
	@ResponseBody
	public void uploadInvitaionProposal(@PathVariable Long invitationId, @RequestParam("file") MultipartFile file) {
		fileService.uploadInvitationProposal(invitationId, file);
	}

}
