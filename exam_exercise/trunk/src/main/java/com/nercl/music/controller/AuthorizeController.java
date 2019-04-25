package com.nercl.music.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;
import com.nercl.music.entity.authorize.AuthorizeRecord;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.exception.CommonLogicException;
import com.nercl.music.service.AuthorizeService;

@Controller
public class AuthorizeController {

	@Autowired
	private AuthorizeService authorizeService;

	@GetMapping("/authorizes")
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AuthorizeRecord> authorizes = authorizeService.list(page);
		model.addAttribute("authorizes", authorizes);
		return "authorize/authorizes";
	}

	@GetMapping("/authorize")
	public String toSave() {
		return "authorize/authorize";
	}

	@PostMapping("/authorize")
	public String save(String topicId, String toAuthorizerId, HttpSession session) {
		Manager manager = (Manager) session.getAttribute("manager");
		if (null == manager) {
			return "redirect:/session";
		}
		if (Strings.isNullOrEmpty(topicId) || Strings.isNullOrEmpty(toAuthorizerId)) {
			throw CommonLogicException.create(500, "topicId is null or toAuthorizerId is null");
		}
		boolean success = authorizeService.save(topicId, manager.getPersonId(), toAuthorizerId);
		if (success) {
			return "redirect:/authorizes";
		}
		return "redirect:/authorizes";
	}

	@GetMapping(value = "/authorizes", params = {"exercise_id"})
	public String get(@RequestParam(value = "exercise_id") String exerciseId,
	                  @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AuthorizeRecord> records = authorizeService.list(exerciseId, page);
		model.addAttribute("records", records);
		return "authorize/authorizes";
	}

	@GetMapping(value = "/authorizes", params = {"title", "subjectType", "name", "login"})
	public String query(@RequestParam(value = "title") String title, @RequestParam(value = "subjectType") Integer subjectType,
	                    @RequestParam(value = "name") String name, @RequestParam(value = "login") String login,
	                    @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AuthorizeRecord> authorizes = authorizeService.query(title, subjectType, name, login, page);
		model.addAttribute("authorizes", authorizes);
		model.addAttribute("title", title);
		model.addAttribute("name", name);
		model.addAttribute("login", login);
		return "authorize/authorizes";
	}

}
