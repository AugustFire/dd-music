package com.nercl.music.cloud.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.user.Login;
import com.nercl.music.cloud.entity.user.Person;
import com.nercl.music.cloud.service.LoginService;
import com.nercl.music.cloud.service.PersonService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;

@RestController
public class LoginController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/login", produces = JSON_PRODUCES)
	public Map<String, Object> login(String login, String password, String ip, HttpServletRequest request)
			throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(login)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "login is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "password is null");
			return ret;
		}
		boolean success = this.loginService.authenticate(login, password);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "login or password is wrong");
			return ret;
		}
		Login lgin = this.loginService.getByLogin(login);
		if (null == lgin) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "person is null");
			return ret;
		}
		Person person = this.personService.getByLogin(login);
		if (null == person) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "person is null");
			return ret;
		}
		String loginToken = loginService.setLoginToken(login);
		if (Strings.isNullOrEmpty(loginToken)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "set token failed");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", person.getId());
		ret.put("name", person.getName());
		ret.put("is_teacher", person.getIsTeacher());
		ret.put("age", person.getAge());
		ret.put("email", person.getEmail());
		ret.put("phone", person.getPhone());
		ret.put("gender", person.getGender());
		ret.put("role", person.getRole());
		ret.put("login_token", loginToken);
		Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, person.getId());
		if (null != classes ) {
			if(null != classes.get("school_id")){
				ret.put("school_id", classes.getOrDefault("school_id", ""));
				ret.put("school_name", classes.getOrDefault("school_name", ""));
			}
			if(null != classes.get("classes")){
				List<Map<String,String>> cls = (List<Map<String, String>>) classes.get("classes");
				if(null!=cls.get(0)){
					ret.put("class_id", cls.get(0).getOrDefault("class_id", ""));
					ret.put("class_name", cls.get(0).getOrDefault("class_name", ""));
				}
			}
			if(null != classes.get("grades")){
				List<Map<String,String>> grades = (List<Map<String, String>>) classes.get("grades");
				if(null!=grades.get(0)){
					ret.put("grade_id", grades.get(0).getOrDefault("grade_id", ""));
					ret.put("grade_name", grades.get(0).getOrDefault("grade_name", ""));
					ret.put("grade_code", grades.get(0).getOrDefault("grade_code", ""));
				}
			}
		}
		return ret;
	}

	@GetMapping(value = "/logout", produces = JSON_PRODUCES)
	public Map<String, Object> logout(String uid) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户id为空");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据用户Id获取用户登陆信息
	 * 
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/person", produces = JSON_PRODUCES)
	public Map<String, Object> getByPerson(String uid) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户id为空");
			return ret;
		}
		Login byPerson = loginService.getByPerson(uid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("login", byPerson);
		return ret;
	}

	/**
	 * 用户认证
	 */
	@PostMapping(value = "/authenticate", produces = JSON_PRODUCES)
	public Map<String, Object> authenticate(String login, String password) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "login or password is null");
			return ret;
		}
		boolean success = this.loginService.authenticate(login, password);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "login or password is wrong");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 根据用户Id获取用户登陆信息
	 * 
	 * @param uid
	 *            用户id
	 */
	@GetMapping(value = "/login", produces = JSON_PRODUCES)
	public Map<String, Object> getByLogin(String login) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(login)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "login is null");
			return ret;
		}
		Login lgn = this.loginService.getByLogin(login);
		if (null != lgn) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("login", lgn);
			return ret;
		}
		return ret;
	}

	/**
	 * 保存用户的token
	 * 
	 * @param login
	 */
	@PostMapping(value = "/login_token", produces = JSON_PRODUCES)
	public Map<String, Object> setLoginToken(String login) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(login)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "login is null");
			return ret;
		}
		String loginToken = loginService.setLoginToken(login);
		if (null != loginToken) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("loginToken", loginToken);
			return ret;
		}
		return ret;
	}

	/**
	 * 保存用户的token
	 * 
	 * @param login
	 */
	@PostMapping(value = "/login_save", produces = JSON_PRODUCES)
	public Map<String, Object> saveLogin(Login login) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == login) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "login is null");
			return ret;
		}
		boolean save = loginService.save(login);
		if (save) {
			ret.put("code", CList.Api.Client.OK);
			return ret;
		}
		return ret;
	}
}
