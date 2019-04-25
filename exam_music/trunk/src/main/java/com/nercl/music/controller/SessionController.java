package com.nercl.music.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Manager;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.ExpertService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.MFileService;
import com.nercl.music.service.ManagerService;

@Controller
public class SessionController implements MessageController {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ExamineeService examineeService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ExpertService expertService;

	@Autowired
	private LoginService loginService;

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

	/**
	 * 登录
	 * 
	 */
	@GetMapping("/session")
	public String create() {
		return "session";
	}

	/**
	 * 登录
	 * 
	 */
	@PostMapping("/session")
	public String save(String login, String password, String checkcode, HttpSession session,
	        RedirectAttributes redirectAttr) {
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			return "redirect:/session";
		}
		String code = (String) session.getAttribute("code");
		if (!StringUtils.equalsIgnoreCase(code, checkcode)) {
			sendWarning(messageSource, redirectAttr, "code.error");
			return "redirect:/session";
		}
		boolean authentication = loginService.authenticate(login, password);
		if (authentication) {
			Login lgin = this.loginService.getByLogin(login);
			if (null != lgin) {
				Examinee examinee = this.examineeService.getByLogin(login);
				if (null != examinee) {
					this.setToSession(examinee.getPerson().getName(), lgin, examinee, session);
					return "redirect:/examinee";
				}
				Manager manager = this.managerService.getByLogin(login);
				if (null != manager) {
					this.setToSession(manager.getCode(), lgin, manager, session);
					return "redirect:/examinees";
				}
				Expert expert = this.expertService.getByLogin(login);
				if (null != expert) {
					this.setToSession(expert.getPerson().getName(), lgin, expert, session);
					return "redirect:/examinees";
				}
			}
		}
		sendWarning(messageSource, redirectAttr, "password.error");
		return "redirect:/session";
	}

	private void setToSession(String name, Login lgin, Examinee examinee, HttpSession session) {
		session.setAttribute("name", name);
		session.setAttribute("login", lgin);
		session.setAttribute("examinee", examinee);
	}

	private void setToSession(String code, Login lgin, Manager manager, HttpSession session) {
		session.setAttribute("code", code);
		session.setAttribute("login", lgin);
		session.setAttribute("manager", manager);
	}

	private void setToSession(String name, Login lgin, Expert expert, HttpSession session) {
		session.setAttribute("name", name);
		session.setAttribute("login", lgin);
		session.setAttribute("expert", expert);
	}

	/**
	 * 退出
	 * 
	 */
	@DeleteMapping("/session")
	public String destroy(HttpSession session) {
		session.invalidate();
		return "redirect:/session";
	}

}
