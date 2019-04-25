package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.AbstractGroup;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExpertService;
import com.nercl.music.service.GroupService;
import com.nercl.music.service.LoginService;
import com.nercl.music.util.PatternUtil;

@Controller
public class ExpertController implements BaseController, MessageController {

	@Autowired
	private ExpertService expertService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LoginService loginService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ExamService examService;

	@GetMapping(value = "/experts")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Expert> experts = this.expertService.list(page);
		model.addAttribute("experts", experts);
		return "expert/experts";
	}

	@GetMapping(value = "/experts", params = { "key" })
	public String query(@RequestParam(value = "key") String key,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Expert> experts = null;
		if (StringUtils.isBlank(key)) {
			experts = this.expertService.list(page);
		} else {
			experts = this.expertService.query(key, page);
		}
		model.addAttribute("experts", experts);
		model.addAttribute("key", key);
		return "expert/experts";
	}

	@GetMapping(value = "/expert")
	public String toAdd(Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		return "expert/expert";
	}

	@PostMapping(value = "/expert")
	public String add(String login, String name, String jobTitle, String unit, String phone, String email, String intro,
	        RedirectAttributes redirectAttr) {
		if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(jobTitle)
		        || Strings.isNullOrEmpty(unit) || Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(email)
		        || Strings.isNullOrEmpty(intro)) {
			return "redirect:/expert";
		}
		if (loginService.exsitLogin(login)) {
			sendWarning(messageSource, redirectAttr, "user.exsit");
			return "redirect:/expert";
		}

		if (loginService.exsitPhone(phone)) {
			sendWarning(messageSource, redirectAttr, "phone.exsit");
			return "redirect:/expert";
		}
		if (loginService.exsitEmail(email)) {
			sendWarning(messageSource, redirectAttr, "email.exsit");
			return "redirect:/expert";
		}
		if (!PatternUtil.checkLogin(login)) {
			sendWarning(messageSource, redirectAttr, "username.rule");
			return "redirect:/expert";
		}
		if (!PatternUtil.checkEmail(email)) {
			sendWarning(messageSource, redirectAttr, "email.rule");
			return "redirect:/expert";
		}
		if (!PatternUtil.checkPhone(phone)) {
			sendWarning(messageSource, redirectAttr, "phone.rule");
			return "redirect:/expert";
		}
		this.expertService.save(login, name, jobTitle, unit, phone, email, intro);
		return "redirect:/experts";
	}

	/**
	 * 专家修改页面
	 * 
	 */
	@GetMapping(value = "/expert/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEdit(@PathVariable String id, Model model) {
		Expert expert = expertService.get(id);
		String login = expertService.getLoginByExpertId(id);
		if (expert != null) {
			model.addAttribute("expert", expert);
			model.addAttribute("user", login);
			return "expert/expert";
		}
		return "error/500";
	}

	/**
	 * 专家信息修改
	 *
	 */
	@PostMapping("/expert/{id}")
	public String edit(@PathVariable String id, String login, String phone, String name, String email, String jobTitle,
	        String unit, String intro, RedirectAttributes redirectAttr, HttpServletRequest request) {
		if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(jobTitle)
		        || Strings.isNullOrEmpty(unit) || Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(email)) {
			return "redirect:/expert";
		}
		Expert expert = expertService.get(id);
		if (expert != null && expert.getPerson() != null) {
			Login user = loginService.getByPerson(expert.getPersonId());
			if (loginService.exsitLogin(login) && !(user.getLogin().equals(login))) {
				sendWarning(messageSource, redirectAttr, "user.exsit");
				return "redirect:/expert/" + id;
			}

			if (loginService.exsitPhone(phone) && !(expert.getPerson().getPhone().equals(phone))) {
				sendWarning(messageSource, redirectAttr, "phone.exsit");
				return "redirect:/expert/" + id;
			}
			if (loginService.exsitEmail(email) && !(expert.getPerson().getEmail().equals(email))) {
				sendWarning(messageSource, redirectAttr, "email.exsit");
				return "redirect:/expert/" + id;
			}
		}
		if (!PatternUtil.checkLogin(login)) {
			sendWarning(messageSource, redirectAttr, "username.rule");
			return "redirect:/expert/" + id;
		}
		if (!PatternUtil.checkEmail(email)) {
			sendWarning(messageSource, redirectAttr, "email.rule");
			return "redirect:/expert/" + id;
		}
		if (!PatternUtil.checkPhone(phone)) {
			sendWarning(messageSource, redirectAttr, "phone.rule");
			return "redirect:/expert/" + id;
		}
		this.expertService.update(id, login, name, jobTitle, unit, phone, email, intro);
		return "redirect:/experts";
	}

	/**
	 * 删除专家
	 *
	 */
	@GetMapping("/expert/delete/{id}")
	@ResponseBody
	public boolean delete(@PathVariable String id) {
		this.expertService.delete(id);
		return true;
	}

	/**
	 * 多条件查询专家
	 * 
	 * @param name
	 * @param room
	 * @param point
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/experts", params = { "name", "job_title", "unit", "phone", "email" })
	@RequiredPrivilege(Role.MANAGER)
	public String query(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "job_title", required = false) String job_title,
	        @RequestParam(value = "unit", required = false) String unit,
	        @RequestParam(value = "phone", required = false) String phone,
	        @RequestParam(value = "email", required = false) String email,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Expert> experts = this.expertService.listByAttributes(page, name, job_title, unit, email, phone);
		model.addAttribute("experts", experts);
		return "expert/experts";
	}

	@GetMapping("/expert/groups")
	public String getGroups(Model model) {
		List<AbstractGroup> groups = this.groupService.getExpertGroups();
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("currentYear", LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("groups", groups);
		return "expert/groups";
	}

	@GetMapping(value = "/expert/groups", params = { "exam_id", "exam_room" })
	public String queryGroups(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_room", required = false) String examRoom,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<AbstractGroup> groups = this.groupService.queryExpertGroups(examId, examRoom, page);
		model.addAttribute("currentYear", LocalDate.now().getYear());
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("groups", groups);
		return "expert/groups";
	}

	@GetMapping("/expert/group")
	public String toNewGroup(Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		return "expert/group";
	}

	@PostMapping("/expert/group")
	public String newGroup(String title, String examId) {
		this.groupService.save(title, examId, false);
		return "redirect:/expert/groups";
	}

	@GetMapping("/expert/group/experts")
	@RequiredPrivilege(Role.MANAGER)
	public String experts(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model,
	        HttpServletRequest request) {
		List<Expert> experts = this.expertService.list(page);
		model.addAttribute("experts", experts);
		return "expert/experts2";
	}

	@PutMapping("/expert/set_group")
	@ResponseBody
	public boolean setGroup(String[] eids, String gid) {
		if (null == eids || StringUtils.isBlank(gid)) {
			return false;
		}
		return this.expertService.setGroup(eids, gid);
	}

	@GetMapping("/expert/examinee_groups")
	@RequiredPrivilege(Role.MANAGER)
	public String examineeGroups(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
	        Model model, HttpServletRequest request) {
		List<AbstractGroup> groups = this.groupService.getExamineeGroups();
		model.addAttribute("groups", groups);
		return "expert/egroups";
	}

	@PutMapping("/expert/related_groups")
	@ResponseBody
	public boolean relatedGroups(String[] expertGroupIds, String[] examineeGroupIds) {
		if (examineeGroupIds == null || expertGroupIds == null) {
			return false;
		}
		return this.groupService.relatedExamineeExpertGroups(examineeGroupIds, expertGroupIds);
	}

}
