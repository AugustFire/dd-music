package com.nercl.music.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamExamPaper;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamExamPaperService;
import com.nercl.music.service.ExamPaperQuestionService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamPointService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExpertMachineWeightService;

@Controller
public class ExamController implements MessageController {

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamExamPaperService examExamPaperService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private ExpertMachineWeightService expertMachineWeightService;

	@Autowired
	private ExamPointService examPointService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@GetMapping(value = "/exams")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Exam> exams = this.examService.list(page);
		Map<String, List<ExamPaper>> examPaperMap = Maps.newHashMap();
		exams.forEach(exam -> {
			examPaperMap.put(exam.getId(), this.examPaperService.getByExam(exam.getId()));
		});
		model.addAttribute("exams", exams);
		model.addAttribute("examPaperMap", examPaperMap);
		return "exam/exams";
	}

	@GetMapping(value = "/exams", params = { "year" })
	@ResponseBody
	public List<Map<String, String>> josn(@RequestParam(value = "year") Integer year, Model model) {
		Exam exam = examService.getUsedToExam(year);
		List<Map<String, String>> ret = Lists.newArrayList();
		if (null != exam) {
			Map<String, String> map = Maps.newHashMap();
			map.put("id", exam.getId());
			map.put("title", exam.getTitle());
			ret.add(map);
		}
		return ret;
	}

	/**
	 * 录入页面
	 * 
	 */
	@GetMapping("/exam")
	@RequiredPrivilege(Role.MANAGER)
	public String toAdd(Model model) {
		List<ExamPoint> examPoints = this.examPointService.list();
		model.addAttribute("examPoints", examPoints);
		return "exam/exam";
	}

	/**
	 * 考试录入
	 * 
	 * @param title
	 * @param intro
	 * @param startAt
	 * @param endAt
	 * @return
	 */
	@PostMapping("/exam")
	@RequiredPrivilege(Role.MANAGER)
	public String add(String id, String title, String intro, Date startAt, Date endAt, String[] pids) {
		if (Strings.isNullOrEmpty(title) || null == startAt || null == endAt) {
			return "redirect:/exam";
		}
		if (endAt.after(startAt)) {
			this.examService.save(title, intro, startAt, endAt, pids);
		}
		return "redirect:/exams";
	}

	@PostMapping("/exam2")
	@RequiredPrivilege(Role.MANAGER)
	public String add2(Date startAt, Date endAt, String title, String intro, String[] pids, String yueliTitle,
	        Integer yueliScore, Integer yueliResolvedTime, Integer yueliSingleTotalScore, Integer yueliSingleNum,
	        Integer yueliSingleScore, Integer yueliMultiTotalScore, Integer yueliMultiNum, Integer yueliMultiScore,
	        Integer yueliShortTotalScore, Integer yueliShortNum, Integer yueliShortScore, String tingyinTitle,
	        Integer tingyinResolvedTime, Integer tingyinShortTotalScore, Integer tingyinShortNum,
	        Integer tingyinShortScore, String lookSingTitle, Integer lookSingResolvedTime, Integer lookSingTotalScore,
	        Integer lookSingNum, Integer lookSingScore) {
		if (Strings.isNullOrEmpty(title) || null == startAt || null == endAt) {
			return "redirect:/exam";
		}
		if (endAt.after(startAt)) {
			Exam exam = this.examService.save(title, intro, startAt, endAt, pids);
			this.examPaperQuestionService.auto(exam.getId(), yueliTitle, yueliScore, yueliResolvedTime,
			        yueliSingleTotalScore, yueliSingleNum, yueliSingleScore, yueliMultiTotalScore, yueliMultiNum,
			        yueliMultiScore, yueliShortTotalScore, yueliShortNum, yueliShortScore, tingyinTitle,
			        tingyinResolvedTime, tingyinShortTotalScore, tingyinShortNum, tingyinShortScore, lookSingTitle,
			        lookSingResolvedTime, lookSingTotalScore, lookSingNum, lookSingScore);
		}
		return "redirect:/exams";
	}

	/**
	 * 编辑页面
	 * 
	 */
	@GetMapping(value = "/exam/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEdit(@PathVariable String id, Model model) {
		Exam exam = examService.get(id);
		List<ExamPoint> examPoints = this.examPointService.list();
		model.addAttribute("exam", exam);
		model.addAttribute("examPoints", examPoints);
		return "exam/exam";
	}

	/**
	 * 考试编辑
	 * 
	 * @param title
	 * @param intro
	 * @param startAt
	 * @param endAt
	 * @return
	 */
	@PostMapping("/exam/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String edit(@PathVariable String id, String title, String intro, Date startAt, Date endAt, String[] pids) {
		if (Strings.isNullOrEmpty(title) || null == startAt || null == endAt) {
			return "redirect:/exam/" + id;
		}
		if (endAt.after(startAt)) {
			this.examService.update(id, title, intro, startAt, endAt, pids);
		}
		return "redirect:/exams";
	}

	@GetMapping(value = "/exam/{id}/set_paper")
	@RequiredPrivilege(Role.MANAGER)
	public String toSetPaper(@PathVariable String id, Model model) {
		Exam exam = examService.get(id);
		if (null != exam) {
			List<ExamExamPaper> examExamPapers = this.examExamPaperService.getByExam(id);
			model.addAttribute("exam", exam);
			Map<String, Integer> weightMap = Maps.newHashMap();
			model.addAttribute("weightMap", weightMap);
			List<ExamPaper> examPapers = examExamPapers.stream().map(examExamPaper -> {
				weightMap.put(examExamPaper.getExamPaperId(), examExamPaper.getWeight());
				if (CList.Api.SubjectType.LOOK_SING.equals(examExamPaper.getExamPaper().getSubjectType())) {
					model.addAttribute("expertMachineWeight", this.expertMachineWeightService.get(id));
				}
				return examExamPaper.getExamPaper();
			}).collect(Collectors.toList());
			model.addAttribute("examPapers", examPapers);
		}
		return "exam/examPaper";
	}

	@PostMapping(value = "/exam/{id}/set_paper")
	@RequiredPrivilege(Role.MANAGER)
	public String setPaper(@PathVariable String id, String[] pids, Integer[] weights, Integer machineWeight,
	        Integer expertWeight, Model model) {
		if (null == pids || pids.length < 1) {
			return "redirect:/exam/" + id + "/set_paper";
		}
		List<Integer> ws = Arrays.stream(weights).filter(weight -> weight != null && weight >= 0)
		        .collect(Collectors.toList());
		if (pids.length != ws.size()) {
			return "redirect:/exam/" + id + "/set_paper";
		}
		int count = (int) ws.stream().mapToInt(w -> {
			return (int) w;
		}).sum();
		if (count != 100) {
			return "redirect:/exam/" + id + "/set_paper";
		}
		if (null == machineWeight || machineWeight < 0 || null == expertWeight || expertWeight < 0
		        || machineWeight + expertWeight != 100) {
			return "redirect:/exam/" + id + "/set_paper";
		}
		this.examService.setExamPapers(id, pids, weights, machineWeight, expertWeight);
		return "redirect:/exams";
	}

	/**
	 * 删除考试
	 *
	 */
	@GetMapping("/exam/delete/{id}")
	@ResponseBody
	public boolean delete(@PathVariable String id) {
		this.examService.delete(id);
		return true;
	}

	@GetMapping("/exam/{id}/use_to_exam")
	public String usedToExam(@PathVariable String id) {
		this.examService.usedToExam(id);
		return "redirect:/exams";
	}

}
