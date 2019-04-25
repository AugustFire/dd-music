package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.cloud.entity.user.Person;
import com.nercl.music.cloud.entity.user.Person.Degree;
import com.nercl.music.cloud.entity.user.Person.Gender;
import com.nercl.music.cloud.entity.user.Role;
import com.nercl.music.cloud.service.LoginService;
import com.nercl.music.cloud.service.MailRetrieveService;
import com.nercl.music.cloud.service.PersonService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.CloudFileUtil;
import com.nercl.music.util.PatternUtil;

@RestController
public class UserController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private MailRetrieveService mailRetrieveService;

	@Autowired
	private PersonService personService;

	@Autowired
	private CloudFileUtil cloudFileUtil;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${dd-yinyue.user.photo}")
	private String photo;

	@PostMapping(value = "/new", produces = JSON_PRODUCES)
	public Map<String, Object> newPerson4() {
		Map<String, Object> ret = Maps.newHashMap();

		Person person = loginService.saveByEmail("dongguan@qq.com", "东莞老师", "123456", true);
		System.out.println("东莞老师--------" + person.getId());

		person = loginService.saveByEmail("1@qq.com", "1老师", "123456", false);
		System.out.println("1老师--------" + person.getId());

		person = loginService.saveByEmail("2@qq.com", "2老师", "123456", false);
		System.out.println("2老师--------" + person.getId());

		person = loginService.saveByEmail("3@qq.com", "3老师", "123456", false);
		System.out.println("3老师--------" + person.getId());

		person = loginService.saveByEmail("4@qq.com", "4老师", "123456", false);
		System.out.println("4老师--------" + person.getId());

		person = loginService.saveByEmail("5@qq.com", "5老师", "123456", false);
		System.out.println("5老师--------" + person.getId());

		person = loginService.saveByEmail("6@qq.com", "6老师", "123456", false);
		System.out.println("6老师--------" + person.getId());

		person = loginService.saveByEmail("7@qq.com", "7老师", "123456", false);
		System.out.println("7老师--------" + person.getId());

		person = loginService.saveByEmail("8@qq.com", "8老师", "123456", false);
		System.out.println("8老师--------" + person.getId());

		person = loginService.saveByEmail("9@qq.com", "9老师", "123456", false);
		System.out.println("9老师--------" + person.getId());

		person = loginService.saveByEmail("10@qq.com", "10老师", "123456", false);
		System.out.println("10老师--------" + person.getId());

		person = loginService.saveByEmail("11@qq.com", "11老师", "123456", false);
		System.out.println("11老师--------" + person.getId());

		person = loginService.saveByEmail("12@qq.com", "12老师", "123456", false);
		System.out.println("12老师--------" + person.getId());

		person = loginService.saveByEmail("13@qq.com", "13老师", "123456", false);
		System.out.println("13老师--------" + person.getId());

		person = loginService.saveByEmail("14@qq.com", "14老师", "123456", false);
		System.out.println("14老师--------" + person.getId());

		person = loginService.saveByEmail("15@qq.com", "15老师", "123456", false);
		System.out.println("15老师--------" + person.getId());

		person = loginService.saveByEmail("16@qq.com", "16老师", "123456", false);
		System.out.println("16老师--------" + person.getId());

		person = loginService.saveByEmail("17@qq.com", "17老师", "123456", false);
		System.out.println("17老师--------" + person.getId());

		person = loginService.saveByEmail("18@qq.com", "18老师", "123456", false);
		System.out.println("18老师--------" + person.getId());

		person = loginService.saveByEmail("19@qq.com", "19老师", "123456", false);
		System.out.println("19老师--------" + person.getId());

		person = loginService.saveByEmail("20@qq.com", "20老师", "123456", false);
		System.out.println("20老师--------" + person.getId());

		person = loginService.saveByEmail("21@qq.com", "21老师", "123456", false);
		System.out.println("21老师--------" + person.getId());

		person = loginService.saveByEmail("22@qq.com", "22老师", "123456", false);
		System.out.println("22老师--------" + person.getId());

		person = loginService.saveByEmail("23@qq.com", "23老师", "123456", false);
		System.out.println("23老师--------" + person.getId());

		person = loginService.saveByEmail("24@qq.com", "24老师", "123456", false);
		System.out.println("24老师--------" + person.getId());

		person = loginService.saveByEmail("25@qq.com", "25老师", "123456", false);
		System.out.println("25老师--------" + person.getId());

		person = loginService.saveByEmail("26@qq.com", "26老师", "123456", false);
		System.out.println("26老师--------" + person.getId());

		person = loginService.saveByEmail("27@qq.com", "27老师", "123456", false);
		System.out.println("27老师--------" + person.getId());

		person = loginService.saveByEmail("28@qq.com", "28老师", "123456", false);
		System.out.println("28老师--------" + person.getId());

		person = loginService.saveByEmail("29@qq.com", "29老师", "123456", false);
		System.out.println("29老师--------" + person.getId());

		person = loginService.saveByEmail("30@qq.com", "30老师", "123456", false);
		System.out.println("30老师--------" + person.getId());

		person = loginService.saveByEmail("31@qq.com", "31老师", "123456", false);
		System.out.println("31老师--------" + person.getId());

		person = loginService.saveByEmail("32@qq.com", "32老师", "123456", false);
		System.out.println("32老师--------" + person.getId());

		person = loginService.saveByEmail("33@qq.com", "33老师", "123456", false);
		System.out.println("33老师--------" + person.getId());

		person = loginService.saveByEmail("34@qq.com", "34老师", "123456", false);
		System.out.println("34老师--------" + person.getId());

		person = loginService.saveByEmail("35@qq.com", "35老师", "123456", false);
		System.out.println("35老师--------" + person.getId());

		person = loginService.saveByEmail("36@qq.com", "36老师", "123456", false);
		System.out.println("36老师--------" + person.getId());

		person = loginService.saveByEmail("37@qq.com", "37老师", "123456", false);
		System.out.println("37老师--------" + person.getId());

		person = loginService.saveByEmail("38@qq.com", "38老师", "123456", false);
		System.out.println("38老师--------" + person.getId());

		person = loginService.saveByEmail("39@qq.com", "39老师", "123456", false);
		System.out.println("39老师--------" + person.getId());

		person = loginService.saveByEmail("40@qq.com", "40老师", "123456", false);
		System.out.println("40老师--------" + person.getId());

		person = loginService.saveByEmail("41@qq.com", "41老师", "123456", false);
		System.out.println("41老师--------" + person.getId());

		person = loginService.saveByEmail("42@qq.com", "42老师", "123456", false);
		System.out.println("42老师--------" + person.getId());

		person = loginService.saveByEmail("43@qq.com", "43老师", "123456", false);
		System.out.println("43老师--------" + person.getId());

		person = loginService.saveByEmail("44@qq.com", "44老师", "123456", false);
		System.out.println("44老师--------" + person.getId());

		person = loginService.saveByEmail("45@qq.com", "45老师", "123456", false);
		System.out.println("45老师--------" + person.getId());

		person = loginService.saveByEmail("46@qq.com", "46老师", "123456", false);
		System.out.println("46老师--------" + person.getId());

		person = loginService.saveByEmail("47@qq.com", "47老师", "123456", false);
		System.out.println("47老师--------" + person.getId());

		person = loginService.saveByEmail("48@qq.com", "48老师", "123456", false);
		System.out.println("48老师--------" + person.getId());

		person = loginService.saveByEmail("49@qq.com", "49老师", "123456", false);
		System.out.println("49老师--------" + person.getId());

		person = loginService.saveByEmail("50@qq.com", "50老师", "123456", false);
		System.out.println("50老师--------" + person.getId());

		person = loginService.saveByEmail("51@qq.com", "51老师", "123456", false);
		System.out.println("51老师--------" + person.getId());

		person = loginService.saveByEmail("52@qq.com", "52老师", "123456", false);
		System.out.println("52老师--------" + person.getId());

		person = loginService.saveByEmail("53@qq.com", "53老师", "123456", false);
		System.out.println("53老师--------" + person.getId());

		person = loginService.saveByEmail("54@qq.com", "54老师", "123456", false);
		System.out.println("54老师--------" + person.getId());

		person = loginService.saveByEmail("55@qq.com", "55老师", "123456", false);
		System.out.println("55老师--------" + person.getId());

		person = loginService.saveByEmail("56@qq.com", "56老师", "123456", false);
		System.out.println("56老师--------" + person.getId());

		person = loginService.saveByEmail("57@qq.com", "57老师", "123456", false);
		System.out.println("57老师--------" + person.getId());

		person = loginService.saveByEmail("58@qq.com", "58老师", "123456", false);
		System.out.println("58老师--------" + person.getId());

		person = loginService.saveByEmail("59@qq.com", "59老师", "123456", false);
		System.out.println("59老师--------" + person.getId());

		person = loginService.saveByEmail("60@qq.com", "60老师", "123456", false);
		System.out.println("60老师--------" + person.getId());

		person = loginService.saveByEmail("61@qq.com", "61老师", "123456", false);
		System.out.println("61老师--------" + person.getId());

		person = loginService.saveByEmail("62@qq.com", "62老师", "123456", false);
		System.out.println("62老师--------" + person.getId());

		person = loginService.saveByEmail("63@qq.com", "63老师", "123456", false);
		System.out.println("63老师--------" + person.getId());

		person = loginService.saveByEmail("64@qq.com", "64老师", "123456", false);
		System.out.println("64老师--------" + person.getId());

		person = loginService.saveByEmail("65@qq.com", "65老师", "123456", false);
		System.out.println("65老师--------" + person.getId());

		person = loginService.saveByEmail("66@qq.com", "66老师", "123456", false);
		System.out.println("66老师--------" + person.getId());

		person = loginService.saveByEmail("67@qq.com", "67老师", "123456", false);
		System.out.println("67老师--------" + person.getId());

		person = loginService.saveByEmail("68@qq.com", "68老师", "123456", false);
		System.out.println("68老师--------" + person.getId());

		person = loginService.saveByEmail("69@qq.com", "69老师", "123456", false);
		System.out.println("69老师--------" + person.getId());

		person = loginService.saveByEmail("70@qq.com", "70老师", "123456", false);
		System.out.println("70老师--------" + person.getId());

		person = loginService.saveByEmail("71@qq.com", "71老师", "123456", false);
		System.out.println("71老师--------" + person.getId());

		person = loginService.saveByEmail("72@qq.com", "72老师", "123456", false);
		System.out.println("72老师--------" + person.getId());

		person = loginService.saveByEmail("73@qq.com", "73老师", "123456", false);
		System.out.println("73老师--------" + person.getId());

		person = loginService.saveByEmail("74@qq.com", "74老师", "123456", false);
		System.out.println("74老师--------" + person.getId());

		person = loginService.saveByEmail("75@qq.com", "75老师", "123456", false);
		System.out.println("75老师--------" + person.getId());

		person = loginService.saveByEmail("76@qq.com", "76老师", "123456", false);
		System.out.println("76老师--------" + person.getId());

		person = loginService.saveByEmail("77@qq.com", "77老师", "123456", false);
		System.out.println("77老师--------" + person.getId());

		person = loginService.saveByEmail("78@qq.com", "78老师", "123456", false);
		System.out.println("78老师--------" + person.getId());

		person = loginService.saveByEmail("79@qq.com", "79老师", "123456", false);
		System.out.println("79老师--------" + person.getId());

		person = loginService.saveByEmail("80@qq.com", "80老师", "123456", false);
		System.out.println("80老师--------" + person.getId());

		person = loginService.saveByEmail("81@qq.com", "81老师", "123456", false);
		System.out.println("81老师--------" + person.getId());

		person = loginService.saveByEmail("82@qq.com", "82老师", "123456", false);
		System.out.println("82老师--------" + person.getId());

		person = loginService.saveByEmail("83@qq.com", "83老师", "123456", false);
		System.out.println("83老师--------" + person.getId());

		person = loginService.saveByEmail("84@qq.com", "84老师", "123456", false);
		System.out.println("84老师--------" + person.getId());

		person = loginService.saveByEmail("85@qq.com", "85老师", "123456", false);
		System.out.println("85老师--------" + person.getId());

		person = loginService.saveByEmail("86@qq.com", "86老师", "123456", false);
		System.out.println("86老师--------" + person.getId());

		person = loginService.saveByEmail("87@qq.com", "87老师", "123456", false);
		System.out.println("87老师--------" + person.getId());

		person = loginService.saveByEmail("88@qq.com", "88老师", "123456", false);
		System.out.println("88老师--------" + person.getId());

		person = loginService.saveByEmail("89@qq.com", "89老师", "123456", false);
		System.out.println("89老师--------" + person.getId());

		person = loginService.saveByEmail("90@qq.com", "90老师", "123456", false);
		System.out.println("90老师--------" + person.getId());

		person = loginService.saveByEmail("91@qq.com", "91老师", "123456", false);
		System.out.println("91老师--------" + person.getId());

		person = loginService.saveByEmail("92@qq.com", "92老师", "123456", false);
		System.out.println("92老师--------" + person.getId());

		person = loginService.saveByEmail("93@qq.com", "93老师", "123456", false);
		System.out.println("93老师--------" + person.getId());

		person = loginService.saveByEmail("94@qq.com", "94老师", "123456", false);
		System.out.println("94老师--------" + person.getId());

		person = loginService.saveByEmail("95@qq.com", "95老师", "123456", false);
		System.out.println("95老师--------" + person.getId());

		person = loginService.saveByEmail("96@qq.com", "96老师", "123456", false);
		System.out.println("96老师--------" + person.getId());

		person = loginService.saveByEmail("97@qq.com", "97老师", "123456", false);
		System.out.println("97老师--------" + person.getId());

		person = loginService.saveByEmail("98@qq.com", "98老师", "123456", false);
		System.out.println("98老师--------" + person.getId());

		person = loginService.saveByEmail("99@qq.com", "99老师", "123456", false);
		System.out.println("99老师--------" + person.getId());

		person = loginService.saveByEmail("100@qq.com", "100老师", "123456", false);
		System.out.println("100老师--------" + person.getId());
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 获取用户
	 * 
	 * @param uid传personId
	 */
	@GetMapping(value = "/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		Person person = personService.get(uid);
		if (null == person) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "user is null");
			return ret;
		}
		// String photoId = person.getPhoto();
		// if (!Strings.isNullOrEmpty(photoId)) {
		// Map<String, Object> tfile = cloudFileUtil.getResource(photoId);
		// String ext = (String) tfile.get("ext");
		// if (!Strings.isNullOrEmpty(ext)) {
		// byte[] bytes = cloudFileUtil.downloadBytes(photoId, ext);
		// if (null != bytes) {
		// ret.put("img", Base64.getEncoder().encodeToString(bytes));
		// }
		// }
		// }
		ret.put("code", CList.Api.Client.OK);
		ret.put("person", person);
		return ret;
	}

	@GetMapping(value = "user2/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> get2(@PathVariable String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		Person person = personService.get(uid);
		if (null == person) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "user is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", person.getId());
		ret.put("name", person.getName());
		Integer age = person.getAge();
		ret.put("age", null == age ? 25 : age);
		ret.put("man", Gender.MAN == person.getGender());
		Gender gender = person.getGender();
		ret.put("gender", null == gender ? Gender.WOMAN : gender);
		Degree degree = person.getDegree();
		ret.put("degree", null == degree ? Degree.BEN_KE : degree);
		ret.put("graduate_school", Strings.nullToEmpty(person.getGraduateSchool()));
		Integer workYear = person.getWorkYear();
		ret.put("worked_years", null == workYear ? 2012 : workYear);
		ret.put("is_grade_master", Role.GRADE_MASTER == person.getRole());
		return ret;
	}

	/**
	 * 获取用户头像
	 * 
	 */
	@GetMapping(value = "/{uid}/photo", produces = JSON_PRODUCES)
	public Map<String, Object> getPhoto(@PathVariable String uid) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		Person person = personService.get(uid);
		if (null == person) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "user is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("uid", uid);
		String tfileId = person.getPhoto();
		if (Strings.isNullOrEmpty(tfileId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "photo is null");
			return ret;
		}
		Map<String, Object> tfile = cloudFileUtil.getResource(tfileId);
		if (null == tfile || tfile.isEmpty()) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "photo is null");
			return ret;
		}
		String ext = (String) tfile.getOrDefault("ext", "");
		if (Strings.isNullOrEmpty(ext)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "photo is null");
			return ret;
		}
		byte[] bytes = cloudFileUtil.downloadBytes(tfileId, ext);
		if (null == bytes) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "photo is null");
			return ret;
		}
		ret.put("photo", Base64.getEncoder().encodeToString(bytes));
		return ret;
	}

	/**
	 * 获取用户
	 */
	@GetMapping(value = "/users/{uids}", produces = JSON_PRODUCES)
	public Map<String, Object> get(@PathVariable String[] uids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == uids || uids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uids is null");
			return ret;
		}
		List<Map<String, Object>> users = Lists.newArrayList();
		ret.put("code", CList.Api.Client.OK);
		ret.put("users", users);
		Arrays.stream(uids).forEach(uid -> {
			Person person = personService.get(uid);
			if (null != person) {
				Map<String, Object> umap = Maps.newHashMap();
				umap.put("id", uid);
				umap.put("name", person.getName());
				umap.put("phone", person.getPhone());
				umap.put("email", person.getEmail());
				umap.put("age", person.getAge());
				umap.put("gender", person.getGender());
				users.add(umap);
			}
		});
		return ret;
	}

	/**
	 * 注册
	 */
	@PostMapping(value = "/register", produces = JSON_PRODUCES)
	public Map<String, Object> register(String phone, String password, Boolean isTeacher) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(phone)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "phone is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "password is null");
			return ret;
		}
		boolean exist = this.loginService.exsitPhone(phone);
		if (exist) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "phone is exist");
			return ret;
		}
		boolean success = loginService.save(phone, password, isTeacher);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("desc", "register success");
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "register fail");
		}
		return ret;
	}

	/**
	 * 注册
	 */
	@PostMapping(value = "/register/email", produces = JSON_PRODUCES)
	public Map<String, Object> register2(String email, String code, String password, Boolean isTeacher) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is null");
			return ret;
		}
		if (!PatternUtil.checkEmail(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is not invalid");
			return ret;
		}
		if (Strings.isNullOrEmpty(code)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "code is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "password is null");
			return ret;
		}
		boolean exist = this.loginService.exsitEmail(email);
		if (exist) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is exist");
			return ret;
		}
		boolean auth = this.mailRetrieveService.authentication(email, code);
		if (!auth) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "code is valid");
			return ret;
		}
		Person person = loginService.saveByEmail(email, password, isTeacher);
		if (null != person) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("uid", person.getId());
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "register fail");
		}
		return ret;
	}

	/**
	 * 完善信息
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/complete", produces = JSON_PRODUCES)
	public Map<String, Object> complete(String uid, String name, String gender, Integer age, String schoolId,
			String gradeId, String classesId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户id为空");
			return ret;
		}
		Person person = personService.get(uid);
		person.setName(name);
		person.setAge(age);
		if (Person.Gender.MAN.name().equals(gender) || Person.Gender.WOMAN.name().equals(gender)) {
			person.setGender(Gender.valueOf(gender));
		}
		boolean success = this.personService.complete(person);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("uid", person.getId());
			ret.put("name", person.getName());
			ret.put("is_teacher", person.getIsTeacher());
			ret.put("age", person.getAge());
			ret.put("email", person.getEmail());
			ret.put("phone", person.getPhone());
			ret.put("gender", person.getGender());
			ret.put("role", person.getRole());
			Map<String, Object> classes = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class,
					person.getId());
			if (null != classes) {
				if (null != classes.get("school_id")) {
					ret.put("school_id", classes.getOrDefault("school_id", ""));
					ret.put("school_name", classes.getOrDefault("school_name", ""));
				}
				if (null != classes.get("classes")) {
					List<Map<String, String>> cls = (List<Map<String, String>>) classes.get("classes");
					if (null != cls.get(0)) {
						ret.put("class_id", cls.get(0).getOrDefault("class_id", ""));
						ret.put("class_name", cls.get(0).getOrDefault("class_name", ""));
					}
				}
				if (null != classes.get("grades")) {
					List<Map<String, String>> grades = (List<Map<String, String>>) classes.get("grades");
					if (null != grades.get(0)) {
						ret.put("grade_id", grades.get(0).getOrDefault("grade_id", ""));
						ret.put("grade_name", grades.get(0).getOrDefault("grade_name", ""));
						ret.put("grade_code", grades.get(0).getOrDefault("grade_code", ""));
					}
				}
			}
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "complete failed");
		}
		return ret;
	}

	/**
	 * 修改密码
	 */
	@PostMapping(value = "/re_password")
	public Map<String, Object> rePassword(String email, String code, String password) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(code)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "code is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "password is null");
			return ret;
		}
		boolean success = mailRetrieveService.authentication(email, code);
		if (!success) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "code is wrong");
			return ret;
		}
		success = this.loginService.repassword(email, password);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "repassword fail");
		}
		return ret;
	}

	/**
	 * 更新电话
	 */
	@PostMapping(value = "/{uid}/phone")
	public Map<String, Object> updatePhone(String phone) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(phone)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "phone is null");
			return ret;
		}
		this.personService.updatePhone();
		return ret;
	}

	/**
	 * 更新头像
	 */
	@PostMapping(value = "/{uid}/photo")
	public Map<String, Object> updatePhoto(@PathVariable String uid, HttpServletRequest request) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid is null");
			return ret;
		}
		if (!(request instanceof MultipartHttpServletRequest)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no photo found");
			return ret;
		}
		MultipartFile mfile = ((MultipartHttpServletRequest) request).getFile("file");
		if (null == mfile) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no photo found");
			return ret;
		}
		File dir = new File(photo);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String uuid = UUID.randomUUID().toString();
		String filename = mfile.getOriginalFilename();
		String ext = Files.getFileExtension(filename);
		File file = new File(photo + File.separator + uuid + "." + ext);
		InputStream in = mfile.getInputStream();
		FileUtils.copyInputStreamToFile(in, file);
		boolean success = this.personService.updatePhoto(uid, file, filename);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update photo failed");
		}
		return ret;
	}

	/**
	 * 判断电话是否已存在
	 */
	@GetMapping(value = "/phone/exist", produces = JSON_PRODUCES)
	public Map<String, Object> existPhone(String phone) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean exist = this.loginService.exsitPhone(phone);
		ret.put("code", CList.Api.Client.OK);
		ret.put("existEmail", exist);
		return ret;
	}

	/**
	 * 判断邮箱是否已存在
	 */
	@GetMapping(value = "/email/exist", produces = JSON_PRODUCES)
	public Map<String, Object> existEmail(String email) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean exist = this.loginService.exsitEmail(email);
		ret.put("code", CList.Api.Client.OK);
		ret.put("existEmail", exist);
		return ret;
	}

	/**
	 * 发送邮箱验证码
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping(value = "/email/send_code", produces = JSON_PRODUCES)
	public Map<String, Object> sendCode(String email) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is null");
			return ret;
		}
		if (!PatternUtil.checkEmail(email)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "email is not invalid");
			return ret;
		}

		boolean success = this.mailRetrieveService.sendAuthCode(email);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	/**
	 * 校验验证码
	 */
	@PostMapping(value = "/email/check", produces = JSON_PRODUCES)
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
	 * 更新用户信息
	 */
	@PostMapping(value = "/update/{uid}", produces = JSON_PRODUCES)
	public Map<String, Object> userUpdate(@PathVariable String uid, Person person) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(uid) || null == person) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uid or person is null");
			return ret;
		}
		Person oldUser = this.personService.get(uid);
		this.personService.update(oldUser, person);
		ret.put("code", CList.Api.Client.OK);
		ret.put("desc", "update success");
		return ret;
	}

	/**
	 * 根据条件获取用户
	 */
	@GetMapping(value = "/user/users", produces = JSON_PRODUCES)
	public Map<String, Object> findByConditions(Person person) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == person) { // 禁止送空查询所有成员
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "person is null");
			return ret;
		}
		List<Person> listPerson = Lists.newArrayList();
		try {
			listPerson = this.personService.findByConditions(person);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("list_person", listPerson);
		return ret;
	}

	@GetMapping(value = "/users/gender", produces = JSON_PRODUCES)
	public Map<String, Object> getGender(String[] ids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == ids || ids.length < 1) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "ids is null");
			return ret;
		}
		List<Person> persons = personService.get(ids);
		ret.put("code", CList.Api.Client.OK);
		if (null == persons || persons.isEmpty()) {
			return ret;
		}
		Map<String, Integer> genders = Maps.newHashMap();
		persons.forEach(person -> {
			if (Person.Gender.MAN.equals(person.getGender())) {
				Integer count = genders.getOrDefault("MAN", 0);
				genders.put("MAN", ++count);
			} else {
				Integer count = genders.getOrDefault("WOMAN", 0);
				genders.put("WOMAN", ++count);
			}
		});
		ret.putAll(genders);
		return ret;
	}

}
