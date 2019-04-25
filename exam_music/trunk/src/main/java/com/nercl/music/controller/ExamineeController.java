package com.nercl.music.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Enrollment;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.entity.ExamRoom;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.Result;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Person;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.EnrollmentService;
import com.nercl.music.service.ExamExamPaperService;
import com.nercl.music.service.ExamPointService;
import com.nercl.music.service.ExamRoomService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.GroupService;
import com.nercl.music.service.LoginService;
import com.nercl.music.service.MailRetrieveService;
import com.nercl.music.service.PersonService;
import com.nercl.music.service.ResultService;
import com.nercl.music.util.BaiduFaceSDKUtil;
import com.nercl.music.util.PatternUtil;

@Controller
public class ExamineeController implements BaseController, MessageController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private ExamineeService examineeService;

	@Autowired
	private PersonService personService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MailRetrieveService mailRetrieveService;

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPointService examPointService;

	@Autowired
	private ExamRoomService examRoomService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ResultService resultService;

	@Value("${exam_music.examinee.photo}")
	private String photoPath;

	@Value("${exam_music.domain}")
	private String domain;

	@Autowired
	private BaiduFaceSDKUtil baiduFaceSDKUtil;

	@Autowired
	private ExamExamPaperService examExamPaperService;

	@GetMapping(value = "/examinee/photo/{path}")
	public void getBaiduPhoto(@PathVariable String path, HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("image/jpeg");
		File photoFile = new File(this.photoPath + File.separator + path + ".png");
		OutputStream os = null;
		try {
			if (photoFile.exists() && photoFile.isFile()) {
				os = resp.getOutputStream();
				Files.copy(photoFile, os);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 管理员查询所有考生
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/examinees")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model,
	        HttpServletRequest request) throws Exception {
		List<Examinee> examinees = this.examineeService.list(page);
		model.addAttribute("examinees", examinees);
		List<AbstractGroup> groups = this.groupService.getExamineeGroups();
		model.addAttribute("groups", groups);
		return "examinee/examinees";
	}

	/**
	 * 多条件查询考生
	 * 
	 * @param name
	 * @param idcard
	 * @param examNo
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/examinees", params = { "school", "email", "phone", "name", "idcard", "exam_no" })
	@RequiredPrivilege(Role.MANAGER)
	public String query(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "idcard", required = false) String idcard,
	        @RequestParam(value = "exam_no", required = false) String examNo,
	        @RequestParam(value = "school", required = false) String school,
	        @RequestParam(value = "email", required = false) String email,
	        @RequestParam(value = "phone", required = false) String phone,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AbstractGroup> groups = this.groupService.getExamineeGroups();
		model.addAttribute("groups", groups);
		List<Examinee> examinees = this.examineeService.list(school, idcard, name, phone, email, examNo, page);
		model.addAttribute("examinees", examinees);
		model.addAttribute("name", name);
		model.addAttribute("idcard", idcard);
		model.addAttribute("examNo", examNo);
		return "examinee/examinees";
	}

	@GetMapping(value = "/examinees", params = { "exam_id" })
	@ResponseBody
	public String getByExam(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Examinee> examinees = this.examineeService.getByExam(examId, page);
		model.addAttribute("examinees", examinees);
		return "examinee/examinees";
	}

	/**
	 * 注册
	 * 
	 */
	@GetMapping("/register")
	public String toRegister(Model model) {
		return "examinee/register";
	}

	/**
	 * 注册
	 *
	 */
	@PostMapping("/register")
	public String register(String login, String password, String repassword, String name, Integer age, String phone,
	        String gender, String email, String idcard, String examNo, String imgData, String school,
	        RedirectAttributes redirectAttr, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", login);
		map.put("password", password);
		map.put("repassword", repassword);
		map.put("name", name);
		map.put("age", age);
		map.put("phone", phone);
		map.put("gender", gender);
		map.put("email", email);
		map.put("idcard", idcard);
		map.put("examNo", examNo);
		map.put("school", school);
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password) || StringUtils.isBlank(email)
		        || StringUtils.isBlank(phone) || StringUtils.isBlank(imgData)) {
			return "redirect:/register";
		}
		if (!PatternUtil.checkLogin(login)) {
			sendWarning(messageSource, redirectAttr, "username.rule");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		if (!PatternUtil.checkEmail(email)) {
			sendWarning(messageSource, redirectAttr, "email.rule");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		if (!PatternUtil.checkPhone(phone)) {
			sendWarning(messageSource, redirectAttr, "phone.rule");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		if (!PatternUtil.checkIdcard(idcard)) {
			sendWarning(messageSource, redirectAttr, "idcard.rule");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		if (!StringUtils.equals(password, repassword)) {
			sendWarning(messageSource, redirectAttr, "repassword.not.same");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		if (!PatternUtil.checkPassword(password)) {
			sendWarning(messageSource, redirectAttr, "password.rule");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		boolean existLogin = this.loginService.exsitLogin(login);
		if (existLogin) {
			sendWarning(messageSource, redirectAttr, "user.exsit");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		boolean existPhone = this.loginService.exsitPhone(phone);
		if (existPhone) {
			sendWarning(messageSource, redirectAttr, "phone.exsit");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		boolean existEmail = this.loginService.exsitEmail(email);
		if (existEmail) {
			sendWarning(messageSource, redirectAttr, "email.exsit");
			sendExamineeInfo(redirectAttr, map);
			return "redirect:/register";
		}
		String photo = UUID.randomUUID().toString() + ".png";
		byte[] bytes = Base64.getDecoder().decode(imgData.substring(22));
		try {
			FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes),
			        new File(this.photoPath + File.separator + photo));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Login lg = this.examineeService.save(login, password, name, phone, idcard, examNo, school, age, email, gender,
		        photo);
		if (null != lg) {
			String imagePath = this.photoPath + File.separator + photo;
			Person person = this.personService.get(lg.getPersonId());
			this.baiduFaceSDKUtil.faceRegister(imagePath, lg.getId(), person.getName());
			sendNotice(messageSource, redirectAttr, "register.success");
			return "redirect:/session";
		}
		return "redirect:/session";
	}

	/**
	 * 修改页面
	 * 
	 */
	@GetMapping(value = "/examinee/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEdit(@PathVariable String id, Model model) {
		Examinee examinee = examineeService.getById(id);
		if (examinee != null) {
			model.addAttribute("examinee", examinee);
			return "examinee/examinee";
		}
		return "error/500";
	}

	/**
	 * 修改
	 *
	 */
	@PostMapping("/examinee/{id}")
	public String edit(@PathVariable String id, String name, Integer age, String phone, String gender, String email,
	        String school, String idcard, RedirectAttributes redirectAttr, HttpServletRequest request) {
		if (StringUtils.isBlank(email) || StringUtils.isBlank(phone)) {
			return "redirect:/examinee/" + id;
		}
		Examinee examinee = examineeService.getById(id);
		if (examinee != null && examinee.getPerson() != null) {

			if (exsitPhone(phone) && !(examinee.getPerson().getPhone().equals(phone))) {
				sendWarning(messageSource, redirectAttr, "phone.exsit");
				return "redirect:/examinee/" + id;
			}
			if (exsitEmail(email) && !(examinee.getPerson().getEmail().equals(email))) {
				sendWarning(messageSource, redirectAttr, "email.exsit");
				return "redirect:/examinee/" + id;
			}
		}
		if (!PatternUtil.checkEmail(email)) {
			sendWarning(messageSource, redirectAttr, "email.rule");
			return "redirect:/examinee/" + id;
		}
		if (!PatternUtil.checkPhone(phone)) {
			sendWarning(messageSource, redirectAttr, "phone.rule");
			return "redirect:/examinee/" + id;
		}
		if (!PatternUtil.checkIdcard(idcard)) {
			sendWarning(messageSource, redirectAttr, "idcard.rule");
			return "redirect:/examinee/" + id;
		}
		this.examineeService.update(id, name, age, phone, gender, email, school, idcard);
		return "redirect:/examinees";
	}

	/**
	 * 删除考生
	 *
	 */
	@GetMapping("/examinee/delete/{id}")
	@ResponseBody
	public boolean delete(@PathVariable String id) {
		this.examineeService.delete(id);
		return true;
	}

	@GetMapping("/myResult")
	public String myResult(HttpSession session, Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		Examinee exaninee = (Examinee) session.getAttribute("examinee");
		if (null != exam && null != exaninee) {
			List<Result> results = resultService.getExamineeResults(exaninee.getId(), exam.getId());
			model.addAttribute("results", results);
			Map<String, Integer> weightMap = this.examExamPaperService.getWeight(exam.getId());
			model.addAttribute("weightMap", weightMap);
			Integer yueLiScore = null;
			Integer tingYinScore = null;
			Integer looksingScore = null;
			for (Result result : results) {
				if (CList.Api.SubjectType.YUE_LI.equals(result.getExamPaper().getSubjectType())) {
					yueLiScore = result.getScore();
				}
				if (CList.Api.SubjectType.TING_YIN.equals(result.getExamPaper().getSubjectType())) {
					tingYinScore = result.getScore();
				}
				if (CList.Api.SubjectType.LOOK_SING.equals(result.getExamPaper().getSubjectType())) {
					looksingScore = result.getScore();
				}
			}
			model.addAttribute("total_score",
			        this.calculateTotalScore(weightMap, yueLiScore, tingYinScore, looksingScore));
		}
		model.addAttribute("exam", exam);
		return "examinee/result";
	}

	private Integer calculateTotalScore(Map<String, Integer> weightMap, Integer yueLiScore, Integer tingYinScore,
	        Integer looksingScore) {
		yueLiScore = null == yueLiScore ? 0 : yueLiScore;
		tingYinScore = null == tingYinScore ? 0 : tingYinScore;
		looksingScore = null == looksingScore ? 0 : looksingScore;
		Integer yueLiWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.YUE_LI));
		yueLiWeight = null == yueLiWeight ? 0 : yueLiWeight;
		Integer tingYinWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.TING_YIN));
		tingYinWeight = null == tingYinWeight ? 0 : tingYinWeight;
		Integer lookSingWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.LOOK_SING));
		lookSingWeight = null == lookSingWeight ? 0 : lookSingWeight;

		looksingScore = null == looksingScore ? 0 : looksingScore;
		return yueLiScore * yueLiWeight / 100 + tingYinScore * tingYinWeight / 100
		        + looksingScore * lookSingWeight / 100;
	}

	@GetMapping("/examinee")
	public String info(Model model) {
		return "examinee/info";
	}

	@GetMapping(value = "/examinee/{id}/photo")
	public void getPhoto(@PathVariable String id, HttpServletResponse resp) {
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("image/jpeg");
		Examinee examinee = this.examineeService.getById(id);
		if (null == examinee) {
			return;
		}
		File photoFile = new File(this.photoPath + File.separator + examinee.getPhoto());
		OutputStream os = null;
		try {
			if (photoFile.exists() && photoFile.isFile()) {
				os = resp.getOutputStream();
				Files.copy(photoFile, os);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 注册时用于前端判断用户名是否存在
	 * 
	 */
	@GetMapping(value = "/examinee/login/exsit", params = { "login" }, produces = JSON_PRODUCES)
	@ResponseBody
	public boolean exsitLogin(@RequestParam(value = "login") String login) {
		return this.loginService.exsitLogin(login);
	}

	/**
	 * 注册时用于前端判断邮箱是否存在
	 * 
	 */
	@GetMapping(value = "/examinee/email/exsit", params = { "email" }, produces = JSON_PRODUCES)
	@ResponseBody
	public boolean exsitEmail(@RequestParam(value = "email") String email) {
		return this.loginService.exsitEmail(email);
	}

	/**
	 * 注册时用于前端判断手机是否存在
	 * 
	 */
	@GetMapping(value = "/examinee/phone/exsit", params = { "phone" }, produces = JSON_PRODUCES)
	@ResponseBody
	public boolean exsitPhone(@RequestParam(value = "phone") String phone) {
		return this.loginService.exsitPhone(phone);
	}

	/**
	 * 找回密码输入帐号页面
	 * 
	 * @return
	 */
	@GetMapping("/user/find_password")
	public String findPassword(Model model) {
		return "examinee/email";
	}

	/**
	 * 发送含有找回密码地址的邮件
	 * 
	 * @return
	 */
	@PostMapping(value = "/send_email")
	public String sendEmail(String email, Model model, RedirectAttributes redirectAttr) {
		if (!PatternUtil.checkEmail(email)) {
			sendNotice(messageSource, model, "email.error");
			return "redirect:/user/find_password";
		}
		if (!this.loginService.exsitEmail(email)) {
			sendNotice(messageSource, redirectAttr, "email.non-existent");
			return "redirect:/user/find_password";
		}
		String url = this.domain + "/check_email";
		Login login = this.loginService.getByEmail(email);
		if (null != login) {
			mailRetrieveService.findPassword(login.getLogin(), email, url);
			sendNotice(messageSource, redirectAttr, "email.send.success");
			return "redirect:/session";
		}
		return "redirect:/user/find_password";
	}

	/**
	 * 验证找回密码url的合法性
	 * 
	 * @param email
	 * @param sid
	 * @return
	 */
	@GetMapping(value = "/check_email", params = { "email", "sid" })
	public String checkEmail(Model model, @RequestParam(value = "email") String email,
	        @RequestParam(value = "sid") String sid) {
		Login login = this.loginService.getByEmail(email);
		if (login != null) {
			boolean canRePass = mailRetrieveService.authentication(login.getLogin(), sid);
			if (canRePass && login != null) {
				model.addAttribute("user", login.getLogin());
				model.addAttribute("sid", sid);
				return "examinee/repassword";
			}
		}
		return "error/500";
	}

	/**
	 * 保存新密码
	 */
	@PostMapping(value = "/repassword")
	public String rePassword(RedirectAttributes redirectAttr, String login, String sid, String password,
	        String repassword) {
		if (StringUtils.isBlank(password) || !password.equals(repassword)) {
			return "error/500";
		}
		boolean canRePass = mailRetrieveService.authentication(login, sid);
		if (canRePass) {
			boolean success = this.loginService.rePassword(login, password);
			if (success) {
				sendNotice(messageSource, redirectAttr, "repassword.success");
				return "redirect:/session";
			}
		}
		return "error/500";
	}

	// 考生查询自己的考试状态，考点等信息
	@GetMapping("/examInforExaminee")
	@RequiredPrivilege(Role.EXAMINEE)
	public String examInforExaminee(HttpSession session, Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		Examinee examinee = (Examinee) session.getAttribute("examinee");
		if (null != exam && null != examinee) {
			StringBuffer examPointRoom = new StringBuffer("");
			Enrollment enrollment = this.enrollmentService.get(exam.getId(), examinee.getId());
			model.addAttribute("enrollment", enrollment);
			model.addAttribute("exam", exam);
			ExamRoom examRoom = examRoomService.getRoom(enrollment.getExamRoomId());
			model.addAttribute("examRoom", examRoom);
			ExamPoint examPoint = null;
			model.addAttribute("examPoint", examPoint);
			if (examRoom != null) {
				examPoint = examPointService.get(examRoom.getExamPointId());
				if (examPoint != null) {
					examPointRoom.append(examPoint.getName()).append("-").append(examRoom.getTitle());
				}
				model.addAttribute("examPointRoom", examPointRoom);
			}
		}
		return "examinee/examInfo";
	}

	@GetMapping("/examinee/groups")
	public String getGroups(Model model) {
		List<AbstractGroup> groups = this.groupService.getExamineeGroups();
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("currentYear", LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("groups", groups);
		return "examinee/groups";
	}

	@GetMapping(value = "/examinee/groups", params = { "exam_id", "exam_room" })
	public String queryGroups(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_room", required = false) String examRoom,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AbstractGroup> groups = this.groupService.queryExamineeGroups(examId, examRoom, page);
		model.addAttribute("currentYear", LocalDate.now().getYear());
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("groups", groups);
		return "examinee/groups";
	}

	@GetMapping("/examinee/group")
	public String toNewGroup(Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		return "examinee/group";
	}

	@PostMapping("/examinee/group")
	public String newGroup(String title, String examId) {
		this.groupService.save(title, examId, true);
		return "redirect:/examinee/groups";
	}

	@GetMapping("/examinee/group/examinees")
	@RequiredPrivilege(Role.MANAGER)
	public String examinees(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model,
	        HttpServletRequest request) {
		List<Examinee> examinees = this.examineeService.list(page);
		model.addAttribute("examinees", examinees);
		return "examinee/examinees2";
	}

	@PutMapping("/examinee/set_group")
	@ResponseBody
	public boolean setGroup(String[] eids, String gid) {
		if (null == eids || StringUtils.isBlank(gid)) {
			return false;
		}
		return this.examineeService.setGroup(eids, gid);
	}
}
