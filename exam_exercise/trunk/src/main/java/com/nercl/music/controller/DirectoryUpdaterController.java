package com.nercl.music.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nercl.music.service.MFileService;

@Controller
public class DirectoryUpdaterController {

	@Autowired
	private MFileService mfileService;

	/**
	 * update
	 * 
	 */
	@GetMapping("/update")
	@ResponseBody
	public void update() {
		this.mfileService.updatePath();
	}

}
