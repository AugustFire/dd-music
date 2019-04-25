package com.nercl.music.controller;

import com.nercl.music.entity.behavior.DownloadQuestionRecord;
import com.nercl.music.entity.behavior.LoginRecord;
import com.nercl.music.entity.behavior.LogoutRecord;
import com.nercl.music.service.BehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BehaviorController {

	@Autowired
	private BehaviorService behaviorService;

	@GetMapping(value = "/behaviors")
	public String toManager(String name, String email, Integer type, Model model,
	                        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		type = null == type ? 0 : type;
		model.addAttribute("type", type);

		if (type == 0) {
			List<LoginRecord> loginRecords = this.behaviorService.getLoginRecords(name, email, page);
			model.addAttribute("loginRecords", loginRecords);
		} else if (type == 1) {
			List<LogoutRecord> logoutRecords = this.behaviorService.getLogoutRecords(name, email, page);
			model.addAttribute("logoutRecords", logoutRecords);
		} else if (type == 2) {
			List<DownloadQuestionRecord> downloadQuestionRecords = this.behaviorService.getDownloadRecords(name, email, page);
			model.addAttribute("downloadQuestionRecords", downloadQuestionRecords);
		}

		return "behavior/behaviors";
	}
}
