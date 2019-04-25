package com.nercl.music.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nercl.music.entity.WorkDuration;
import com.nercl.music.service.WorkDurationService;

@Controller
public class WorkDurationController {

	@Autowired
	private WorkDurationService workDurationService;

	@GetMapping("/wds")
	public String list(Model model) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		List<WorkDuration> wds = this.workDurationService.list(year);
		model.addAttribute("wds", wds);
		model.addAttribute("year", year);
		return "wds";
	}

	@GetMapping("/wd")
	public String add(Model model) {
		model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
		model.addAttribute("workUnits", WorkDuration.WorkUnit.values());
		return "wd";
	}

	@PostMapping("/wd")
	public String save(WorkDuration wd) {
		this.workDurationService.save(wd);
		return "redirect:/wds";
	}

	@GetMapping(value = "/wds", params = { "year" })
	public String getByYear(Model model, @RequestParam(value = "year") int year) {
		List<WorkDuration> wds = this.workDurationService.list(year);
		model.addAttribute("wds", wds);
		model.addAttribute("year", year);
		return "wds";
	}

}
