package com.nercl.music.api;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.BehaviorService;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.MailRetrieveService;
import com.nercl.music.util.PatternUtil;

@RestController
public class ApiLoginController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private ExerciserService exerciserService;

	@Autowired
	private BehaviorService behaviorService;

	@Autowired
	private MailRetrieveService mailRetrieveService;

	@PostMapping(value = "/api/login", produces = JSON_PRODUCES)
	public Map<String, Object> login(String login, String password, String ip, HttpServletRequest request)
	        throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户名或者密码为空");
			return ret;
		}
		boolean success = this.loginService.authenticate(login, password);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "用户名或者密码错误");
			return ret;
		}
		Login lgin = this.loginService.getByLogin(login);
		if (null == lgin) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者不存在");
			return ret;
		}
		Exerciser exerciser = this.exerciserService.getByLogin(login);
		if (null == exerciser) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者不存在");
			return ret;
		}
		String loginToken = loginService.setLoginToken(login);
		if (Strings.isNullOrEmpty(loginToken)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "设置登录token失败");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("exerciser_id", exerciser.getPersonId());
		ret.put("name", exerciser.getPerson().getName());
		ret.put("age", exerciser.getPerson().getAge());
		ret.put("email", exerciser.getPerson().getEmail());
		ret.put("phone", exerciser.getPerson().getPhone());
		ret.put("idcard", exerciser.getIdcard());
		ret.put("gender", exerciser.getPerson().getGender().getDesc());
		ret.put("school", exerciser.getSchool());
		ret.put("token", exerciser.getToken());
		ret.put("login_token", loginToken);

		// 保存登陆记录
		this.behaviorService.saveLogin(exerciser.getPersonId(), ip);

		return ret;
	}

	@GetMapping(value = "/api/logout", produces = JSON_PRODUCES)
	public Map<String, Object> logout(String exerciser_id) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(exerciser_id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "练习者id为空");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);

		// 保存退出记录
		this.behaviorService.saveLogout(exerciser_id);

		return ret;
	}

	@GetMapping(value = "/api/send_code", produces = JSON_PRODUCES)
	public Map<String, Object> sendCode(String email) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "邮箱为空");
			return ret;
		}
		boolean success = PatternUtil.checkEmail(email);
		if (!success) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "邮箱格式不正确");
			return ret;
		}
		boolean exist = this.loginService.exsitEmail(email);
		if (exist) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "邮箱已被注册");
			return ret;
		}
		this.mailRetrieveService.sendAuthCode(email);
		ret.put("code", CList.Api.Client.OK);
		ret.put("desc", "验证码已发送至邮箱");
		return ret;
	}

	@PostMapping(value = "/api/register", produces = JSON_PRODUCES)
	public Map<String, Object> register(String email, String code, String password, String name) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(email) || StringUtils.isBlank(code) || StringUtils.isBlank(password)
		        || StringUtils.isBlank(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "邮箱或者验证码或者密码或者姓名为空");
			return ret;
		}
		boolean success = this.mailRetrieveService.authentication(email, code);
		if (!success) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "验证码不正确");
			return ret;
		}
		success = this.loginService.save(name, email, password);
		if (success) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "注册成功");
			return ret;
		}
		ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		ret.put("desc", "注册失败");
		return ret;
	}

}
