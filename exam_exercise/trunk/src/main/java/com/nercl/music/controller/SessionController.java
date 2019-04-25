package com.nercl.music.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.ManagerService;

@Controller
public class SessionController implements MessageController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private ExerciserService exerciserService;

	/**
	 * 首页
	 */
	@GetMapping("/")
	public String index() {
		return "session";
	}

	/**
	 * 登录
	 */
	@GetMapping("/session")
	public String create() {
		return "session";
	}

	/**
	 * 登录
	 */
	@PostMapping("/session")
	public String save(String login, String password, String checkcode, HttpSession session,
	        RedirectAttributes redirectAttr) {
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			return "redirect:/session";
		}
		// String code = (String) session.getAttribute("code");
		// if (!StringUtils.equalsIgnoreCase(code, checkcode)) {
		// sendWarning(messageSource, redirectAttr, "code.error");
		// return "redirect:/session";
		// }
		boolean success = loginService.authenticate(login, password);
		if (success) {
			Login lgin = this.loginService.getByLogin(login);
			if (null != lgin) {
				Exerciser exerciser = this.exerciserService.getByLogin(login);
				if (null != exerciser) {
					this.setToSession(exerciser.getPerson().getName(), lgin, exerciser, session);
					return "redirect:/answer_records/exerciser";
				}
				Manager manager = this.managerService.getByLogin(login);
				if (null != manager) {
					this.setToSession(manager.getCode(), lgin, manager, session);
					return "redirect:/user/manage";
				}
			}
		}
		sendWarning(messageSource, redirectAttr, "password.error");
		return "redirect:/session";
	}

	private void setToSession(String name, Login lgin, Exerciser exerciser, HttpSession session) {
		session.setAttribute("name", name);
		session.setAttribute("login", lgin);
		session.setAttribute("exerciser", exerciser);
	}

	private void setToSession(String code, Login lgin, Manager manager, HttpSession session) {
		session.setAttribute("code", code);
		session.setAttribute("login", lgin);
		session.setAttribute("manager", manager);
	}

	/**
	 * 退出
	 */
	@DeleteMapping("/session")
	public String destroy(HttpSession session) {
		session.invalidate();
		return "redirect:/session";
	}

}
