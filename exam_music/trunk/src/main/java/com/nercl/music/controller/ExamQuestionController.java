package com.nercl.music.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.MFile;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.UnPassReasonForQuestion;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.MFileService;
import com.nercl.music.util.page.PaginateSupportArray;

@Controller
public class ExamQuestionController {

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private MFileService mfileService;

	@GetMapping(value = "/exam_questions")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "type", required = false) Integer type,
	        @RequestParam(value = "title", required = false) String title,
	        @RequestParam(value = "difficulty", required = false) Float difficulty,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, String status, Model model,
	        HttpServletRequest request) throws Exception {
		List<ExamQuestion> questions = this.examQuestionService.list(type, title, difficulty, page, status);
		model.addAttribute("type", type);
		model.addAttribute("questions", questions);
		model.addAttribute("unPassReasons", UnPassReasonForQuestion.get());
		return "question/questions";
	}

	@GetMapping(value = "/exam_questions", params = { "type", "word" })
	@RequiredPrivilege(Role.MANAGER)
	@ResponseBody
	public Map<String, Object> query(@RequestParam(value = "type") Integer type,
	        @RequestParam(value = "word") String word,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
		List<ExamQuestion> questions = this.examQuestionService.query(type, word, page);
		Map<String, Object> ret = Maps.newHashMap();
		if (questions instanceof PaginateSupportArray) {
			PaginateSupportArray<ExamQuestion> psa = (PaginateSupportArray<ExamQuestion>) questions;
			Map<String, Object> pageMap = Maps.newHashMap();
			ret.put("page", pageMap);
			pageMap.put("total", psa.getTotal());
			pageMap.put("page", psa.getPage());
			pageMap.put("page_size", psa.getPageSize());
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		ret.put("questions", list);
		questions.forEach(question -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", question.getId());
			map.put("title", question.getTitle2());
			map.put("difficulty", question.getDifficulty());
			list.add(map);
		});
		return ret;
	}

	/**
	 * 
	 * @param type
	 * @param title
	 * @param difficulty
	 * @param page
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/exam_question/{questoinId}")
	@RequiredPrivilege(Role.MANAGER)
	public String getQuestion(@PathVariable String questoinId, Model model) throws Exception {
		ExamQuestion question = this.examQuestionService.get(questoinId);
		model.addAttribute("question", question);
		return "question/question";
	}

	@GetMapping(value = "/exam_questions/json", params = { "type" })
	@ResponseBody
	public Map<String, Object> json(@RequestParam(value = "type") Integer type,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		List<ExamQuestion> questions = this.examQuestionService.list(type, page);
		Map<String, Object> ret = Maps.newHashMap();
		if (questions instanceof PaginateSupportArray) {
			PaginateSupportArray<ExamQuestion> psa = (PaginateSupportArray<ExamQuestion>) questions;
			Map<String, Object> pageMap = Maps.newHashMap();
			ret.put("page", pageMap);
			pageMap.put("total", psa.getTotal());
			pageMap.put("page", psa.getPage());
			pageMap.put("page_size", psa.getPageSize());
		}
		List<Map<String, Object>> list = Lists.newArrayList();
		ret.put("questions", list);
		questions.forEach(question -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", question.getId());
			map.put("title", question.getTitle2());
			map.put("difficulty", question.getDifficulty());
			if (null != question.getHasStaff() && question.getHasStaff()) {
				String xmlPath = question.getXmlPath();
				MFile mfile = this.mfileService.getByUrl(xmlPath);
				if (null != mfile) {
					map.put("xml_path", mfile.getId());
				}
			}
			list.add(map);
		});
		return ret;
	}

	@GetMapping(value = "/exam_question/{qid}/pass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean pass(@PathVariable String qid, HttpSession session) {
		Login login = (Login) session.getAttribute("login");
		return this.examQuestionService.pass(qid, login);
	}

	@GetMapping(value = "/exam_question/{qid}/unpass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean unpass(@PathVariable String qid, String reason, HttpSession session) {
		Login login = (Login) session.getAttribute("login");
		return this.examQuestionService.unpass(qid, login, reason);
	}

}
