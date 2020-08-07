package com.allscontracting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/main/users")
	public String users() {
		return "user";
	}

}
