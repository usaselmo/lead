package com.allscontracting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
	
	@GetMapping(path= {"/", ""})
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

}
