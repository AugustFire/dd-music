package com.nercl.music.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamQuestion;
import com.nercl.music.entity.Option;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.UnPassReasonForPaper;
import com.nercl.music.entity.user.Login;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamQuestionService;

@Controller
public class ExamPaperController {

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamQuestionService examQuestionService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@GetMapping(value = "/exam_papers")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ExamPaper> examPapers = this.examPaperService.list(page);
		model.addAttribute("examPapers", examPapers);
		model.addAttribute("unPassReasons", UnPassReasonForPaper.get());
		return "paper/papers";
	}

	@GetMapping(value = "/exam_papers", params = { "name" })
	@ResponseBody
	public String getByName(@RequestParam(value = "name") String name,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		this.examPaperService.get(name, page);
		return "result";
	}

	@GetMapping(value = "/exam_papers", params = { "year" })
	@ResponseBody
	public String getByYear(@RequestParam(value = "year") int year,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		this.examPaperService.get(year);
		return "result";
	}

	@GetMapping(value = "/exam_papers", params = { "exam_id" })
	@ResponseBody
	public List<Map<String, Object>> getByEaxm(@RequestParam(value = "exam_id") String examId) {
		List<ExamPaper> examPapers = this.examPaperService.getByExam(examId);
		List<Map<String, Object>> ret = Lists.newArrayList();
		if (null != examPapers) {
			examPapers.forEach(paper -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", paper.getId());
				map.put("title", paper.getTitle());
				ret.add(map);
			});
		}
		return ret;
	}

	@GetMapping(value = "/exam_paper")
	public String toAdd() {
		return "paper/paper";
	}

	@PostMapping(value = "/exam_paper")
	public String add(String title, Integer year, Integer resolvedTime, Integer subjectType) {
		this.examPaperService.save(title, year, resolvedTime, subjectType);
		return "redirect:/exam_papers";
	}

	/**
	 * 编辑考试
	 * 
	 */
	@GetMapping(value = "/exam_paper/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEdit(@PathVariable String id, Model model) {
		ExamPaper examPaper = this.examPaperService.get(id);
		model.addAttribute("examPaper", examPaper);
		return "paper/paper";
	}

	/**
	 * 编辑考试
	 * 
	 */
	@PostMapping(value = "/exam_paper/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String edit(@PathVariable String id, String title, Integer year, Integer resolvedTime, Integer subjectType) {
		this.examPaperService.update(id, title, year, resolvedTime, subjectType);
		return "redirect:/exam_papers";
	}

	/**
	 * 设置考题
	 * 
	 */
	@GetMapping(value = "/exam_paper/{id}/set_questions")
	@RequiredPrivilege(Role.MANAGER)
	public String toSetQuestions(@PathVariable String id, Model model) {
		ExamPaper examPaper = this.examPaperService.get(id);
		model.addAttribute("examPaper", examPaper);
		return "paper/setQuestions";
	}

	/**
	 * 设置考题
	 * 
	 */
	@PostMapping(value = "/exam_paper/{id}/set_questions")
	@RequiredPrivilege(Role.MANAGER)
	public String setQuestions(@PathVariable String id, String[] questionIds, Model model) {
		if (null == questionIds || questionIds.length < 1) {
			return "redirect:/exam_paper/" + id + "/set_questions";
		}
		this.examPaperQuestionService.setQuestionsToExamPaper(id, questionIds, null);
		return "redirect:/exam_paper/" + id + "/set_paper";
	}

	@GetMapping(value = "/exam_paper/{id}/preview")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public Map<String, Object> preview(@PathVariable String id, Model model) {
		ExamPaper examPaper = this.examPaperService.get(id);
		Map<String, Object> ret = Maps.newHashMap();
		if (null != examPaper) {
			Map<Integer, List<ExamQuestion>> questionMap = this.examQuestionService.getByExamPaper(id);
			Map<String, Integer> scoreMap = this.examPaperQuestionService.getScore(examPaper.getId());
			model.addAttribute("examPaper", examPaper);
			model.addAttribute("questionMap", questionMap);
			model.addAttribute("scoreMap", scoreMap);

			ret.put("exam_title", examPaper.getTitle());
			List<Map<String, Object>> questionMaps = Lists.newArrayList();
			for (Map.Entry<Integer, List<ExamQuestion>> entry : questionMap.entrySet()) {
				List<ExamQuestion> qs = entry.getValue();
				qs.forEach(q -> {
					Map<String, Object> qMap = Maps.newHashMap();
					questionMaps.add(qMap);
					qMap.put("title", q.getTitle2());
					qMap.put("type", q.getQuestionType());
					qMap.put("staff", q.getXmlPath2());
					List<Option> options = q.getOptions();
					if (null != options && !options.isEmpty()) {
						List<Map<String, Object>> optionMaps = Lists.newArrayList();
						qMap.put("options", optionMaps);
						options.forEach(option -> {
					        Map<String, Object> oMap = Maps.newHashMap();
					        optionMaps.add(oMap);
					        if (StringUtils.isNotBlank(option.getContent2())) {
						        oMap.put("content", option.getContent2());
					        } else if (StringUtils.isNotBlank(option.getXmlPath())) {
						        oMap.put("staff", option.getXmlPath2());
					        }
				        });
					}
				});
			}
			ret.put("questions", questionMaps);
		}
		// return "paper/preview";
		return ret;
	}

	@GetMapping(value = "/exam_paper/{id}/set_paper")
	@RequiredPrivilege(Role.MANAGER)
	public String setPaper(@PathVariable String id, Model model) {
		ExamPaper examPaper = this.examPaperService.get(id);
		if (null != examPaper) {
			Map<Integer, List<ExamQuestion>> questionMap = this.examQuestionService.getByExamPaper(id);
			Map<String, Integer> scoreMap = this.examPaperQuestionService.getScore(examPaper.getId());
			model.addAttribute("examPaper", examPaper);
			model.addAttribute("questionMap", questionMap);
			model.addAttribute("scoreMap", scoreMap);
		}
		return "paper/setpaper";
	}

	@PutMapping(value = "/exam_paper/{id}/remove_questions")
	@RequiredPrivilege(Role.MANAGER)
	@ResponseBody
	public boolean remove(@PathVariable String id, String questionId) {
		if (StringUtils.isBlank(questionId)) {
			return false;
		}
		this.examPaperQuestionService.removeQuestions(id, questionId);
		return true;
	}

	@PutMapping(value = "/exam_paper/{id}/set_score")
	@RequiredPrivilege(Role.MANAGER)
	@ResponseBody
	public boolean setScore(@PathVariable String id, String questionId, Integer score) {
		this.examPaperQuestionService.setScore(id, questionId, score);
		return true;
	}

	@GetMapping(value = "/exam_paper/auto")
	public String toAuto() {
		return "paper/auto";
	}

	@PostMapping(value = "/exam_paper/auto")
	@RequiredPrivilege(Role.MANAGER)
	public String auto(String title, Integer totalScore, Integer resolvedTime, Integer subjectType,
	        Integer singleTotalScore, Integer singleNum, Integer singleScore, Integer multiTotalScore, Integer multiNum,
	        Integer multiScore, Integer shortTotalScore, Integer shortNum, Integer shortScore,
	        Integer lookSingTotalScore, Integer lookSingNum, Integer lookSingScore) {
		if (StringUtils.isBlank(title) || null == totalScore || totalScore <= 0 || null == totalScore
		        || subjectType <= 0 || null == resolvedTime || resolvedTime <= 0) {
			return "redirect:/exam_papers";
		}
		this.examPaperQuestionService.auto(title, totalScore, resolvedTime, subjectType, singleNum, singleScore,
		        multiNum, multiScore, shortNum, shortScore, lookSingNum, lookSingScore);
		return "redirect:/exam_papers";
	}

	@GetMapping(value = "/exam_paper/{pid}/pass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean pass(@PathVariable String pid, HttpSession session) {
		Login login = (Login) session.getAttribute("login");
		return this.examPaperService.pass(pid, login);
	}

	@GetMapping(value = "/exam_paper/{pid}/unpass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean unpass(@PathVariable String pid, String reason, HttpSession session) {
		Login login = (Login) session.getAttribute("login");
		return this.examPaperService.unpass(pid, reason, login);
	}

}
