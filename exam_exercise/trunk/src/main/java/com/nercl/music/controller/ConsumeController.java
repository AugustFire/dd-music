package com.nercl.music.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nercl.music.entity.question.ConsumeRecord;
import com.nercl.music.service.ConsumeService;

@Controller
public class ConsumeController {

	@Autowired
	private ConsumeService consumeService;

	@GetMapping(value = "/consumes", params = { "exercise_id" })
	public String get(@RequestParam(value = "exercise_id") String exerciseId,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ConsumeRecord> records = consumeService.list(exerciseId, page);
		model.addAttribute("records", records);
		return "consume/consumes";
	}

	@GetMapping(value = "/consumes", params = { "login" })
	public String query(@RequestParam(value = "login") String login,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ConsumeRecord> records = consumeService.query(login, page);
		model.addAttribute("records", records);
		return "consume/consumes";
	}

}
