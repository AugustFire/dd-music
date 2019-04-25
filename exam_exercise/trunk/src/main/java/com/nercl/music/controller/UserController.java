package com.nercl.music.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.MailRetrieveService;
import com.nercl.music.service.PersonService;
import com.nercl.music.util.page.PaginateSupportArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private MailRetrieveService mailRetrieveService;

	@Autowired
	private PersonService personService;

	@Autowired
	private ExerciserService exerciserService;

	/**
	 * 跳转到注册页面
	 */
	@GetMapping("/register")
	public String toRegister() {
		return "user/register";
	}

	/**
	 * 跳转到忘记密码页面
	 */
	@GetMapping("/user/email/find_password")
	public String toFindPassword() {
		return "user/repass";
	}

	/**
	 * 跳转到用户账号设置页面
	 */
	@GetMapping(value = "/user/set", produces = JSON_PRODUCES)
	public String getUser(HttpSession session, Model model) {
		Login login = (Login) session.getAttribute("login");
		if (null == login) {
			return "redirect:/session";
		}
		Person person = this.personService.get(login.getPersonId());
		boolean isMan = Person.Gender.MAN.equals(person.getGender());
		model.addAttribute("person", person);
		model.addAttribute("isMan", isMan);
		return "user/user";
	}

	/**
	 * 跳转到管理员管理页面
	 */
	@GetMapping(value = "/user/manage")
	public String toManager(String name, String email, Model model,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		// Person person = this.personService.get(id);
		List<Exerciser> exercisers = this.exerciserService.list(page, name, email);
		model.addAttribute("exercisers", exercisers);
		model.addAttribute("name", name);
		model.addAttribute("email", email);
		return "user/user_manage";
	}

	/**
	 * 注册
	 */
	@PostMapping(value = "/register")
	public String register(String username, String email, String password) {
		// Pattern p = Pattern.compile("[a-zA-Z\\d]{6,20}");
		// if (!p.matcher(username).matches()) {
		// sendWarning(messageSource, redirectAttr, "username.rule");
		// return "redirect:/user";
		// }
		// if (!StringUtils.equals(password, repassword)) {
		// sendWarning(messageSource, redirectAttr, "repassword.not.same");
		// return "redirect:/user";
		// }
		// if (!p.matcher(password).matches()) {
		// sendWarning(messageSource, redirectAttr, "password.rule");
		// return "redirect:/user";
		// }
		// boolean isExistUserName = this.loginService.exsitLogin(username);
		// if (isExistUserName) {
		// sendWarning(messageSource, redirectAttr, "user.exsit");
		// return "redirect:/user";
		// }
		boolean success = this.loginService.save(username, email, password);
		if (success) {
			// sendNotice(messageSource, redirectAttr, "register.success");
			return "redirect:/session";
		}
		return "redirect:/register";
	}

	/**
	 * 判断邮箱是否已存在
	 */
	@GetMapping(value = "/user/email/exist", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> existEmail(String email) {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);

		boolean existEmail = this.loginService.exsitEmail(email);
		ret.put("existEmail", existEmail);
		return ret;
	}

	/**
	 * 邮箱，发送验证码
	 */
	@PostMapping(value = "/user/email/send_code", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> sendCode(String email) {
		Map<String, Object> ret = Maps.newHashMap();
		this.mailRetrieveService.sendAuthCode(email);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 校验验证码
	 */
	@PostMapping(value = "/user/email/check", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> rePasswordCheck(String email, String code) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = this.mailRetrieveService.authentication(email, code);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			return ret;
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "验证码错误或者已过期");
		return ret;
	}

	/**
	 * 保存新密码
	 */
	@PostMapping(value = "/user/email/re_password")
	public String rePassword(String email, String code, String password) {
		boolean success = this.loginService.repassword(email, code, password);
		if (success) {
			return "redirect:/session";
		}
		return "redirect:/user/email/find_password";
	}

	/**
	 * 更新用户
	 */
	@PostMapping(value = "/user/update/{id}")
	public String updateUser(@PathVariable String id, Person person) {
		Person oldUser = this.personService.get(id);
		this.personService.update(oldUser, person);
		return "redirect:/user/set";
	}

	@GetMapping(value = "/users", params = { "key" }, produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> query(@RequestParam(value = "key") String key, Model model) {
		List<Person> persons = personService.query(key);
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		if (null != persons) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("users", list);
			persons.forEach(person -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", person.getId());
				map.put("name", person.getName());
				map.put("email", person.getEmail());
				list.add(map);
			});
		}
		return ret;
	}

	@GetMapping(value = "/users", produces = JSON_PRODUCES)
	@ResponseBody
	public Map<String, Object> users(Model model,@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		PaginateSupportArray<Person> persons = personService.list(page);
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
        ret.put("count", persons.getTotal());
        ret.put("page", persons.getPage());
        ret.put("pageSize", persons.getPageSize());
        ret.put("msg", "查询成功");
		if (null != persons) {
			List<Map<String, Object>> list = Lists.newArrayList();
			ret.put("users", list);
			persons.forEach(person -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", person.getId());
				map.put("name", person.getName());
				map.put("email", person.getEmail());
				list.add(map);
			});
		}
		return ret;
	}

}
