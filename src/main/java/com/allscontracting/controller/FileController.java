package com.allscontracting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.allscontracting.model.Lead.Vendor;
import com.allscontracting.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "file")
public class FileController {

	@Autowired FileService fileService;

	@PostMapping(value = "upload")
	public String upload(@RequestParam("file") MultipartFile file, @RequestParam String vendor) {
		try {
			this.fileService.loadLeadFile(file, Vendor.valueOf(vendor));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}

}
