package com.nercl.music.api.mobile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.user.Exerciser;
import com.nercl.music.service.ExerciserService;
import com.nercl.music.service.LoginService;

@RestController
public class ApiExerciserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private ExerciserService exerciserService;

	@Value("${exam_music.exerciser.photo}")
	private String photoPath;

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@PostMapping(value = "/api/mobile/register", produces = JSON_PRODUCES)
	public Map<String, Object> register(String phone, String password) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "手机号码或者密码为空");
			return ret;
		}
		boolean exsitLogin = this.loginService.exsitLogin(phone);
		boolean exsitPhone = this.loginService.exsitPhone(phone);
		if (exsitLogin || exsitPhone) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "手机号码已被注册");
			return ret;
		}
		boolean success = this.exerciserService.register(phone, password);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "注册成功");
		}
		return ret;
	}

	@PostMapping(value = "/api/mobile/login", produces = JSON_PRODUCES)
	public Map<String, Object> login(String phone, String password) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = this.loginService.authenticate(phone, password);
		if (success) {
			Exerciser exerciser = this.exerciserService.getByLogin(phone);
			if (null != exerciser) {
				ret.put("code", CList.Api.Client.OK);
				ret.put("id", exerciser.getId());
				ret.put("phone", phone);
				ret.put("school", exerciser.getSchool());
				ret.put("grade", exerciser.getGrade());
				if (null != exerciser.getPerson()) {
					ret.put("name", exerciser.getPerson().getName());
					ret.put("email", exerciser.getPerson().getEmail());
					ret.put("age", exerciser.getPerson().getAge());
					ret.put("gender", exerciser.getPerson().getGender());
				}
				if (StringUtils.isNotBlank(exerciser.getPhoto())) {
					try {
						ret.put("photo", Base64.getEncoder().encodeToString(
						        Files.toByteArray(new File(this.photoPath + File.separator + exerciser.getPhoto()))));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "该用户不存在");
			}
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "手机号码或者密码错误");
		}
		return ret;
	}

	@PostMapping(value = "/api/mobile/exerciser/complete", produces = JSON_PRODUCES)
	public Map<String, Object> complete(String eid, String name, String email, String gender, Integer age,
	        String school, String grade, String photo) {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(eid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户id为空");
			return ret;
		}
		boolean success = this.exerciserService.complete(eid, name, email, gender, age, school, grade, photo);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("id", eid);
			ret.put("name", name);
			ret.put("school", school);
			ret.put("grade", grade);
			ret.put("email", email);
			ret.put("age", age);
			ret.put("gender", gender);
			ret.put("desc", "用户信息更新成功");
			ret.put("photo", photo);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户信息更新不成功");
		}
		return ret;
	}

}
