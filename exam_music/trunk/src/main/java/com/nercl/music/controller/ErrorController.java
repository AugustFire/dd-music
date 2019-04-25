package com.nercl.music.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	/*@GetMapping("/404")
	public String notFound() {
		System.out.println("--------404---------");
		return "error/404";
	}

	@GetMapping("/500")
	public String serverError() {
		System.out.println("--------500---------");
		return "error/500";
	}*/

	@GetMapping("/noprivilege")
	public String noprivilege() {
		System.out.println("--------/noprivilege---------");
		return "error/noprivilege";
	}

}
