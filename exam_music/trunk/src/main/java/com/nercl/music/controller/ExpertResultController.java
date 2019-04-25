package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExpertResult;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExpertResultService;

@Controller
public class ExpertResultController {

	@Autowired
	private ExpertResultService expertResultService;

	@Autowired
	private ExamService examService;

	@GetMapping(value = "/expert_results")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "exam_no", required = false) String examNo,
	        @RequestParam(value = "expert_name", required = false) String expertName,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		List<ExpertResult> expertResults = this.expertResultService.list(exam.getId(), name, examNo, expertName, page);
		model.addAttribute("currentYear", LocalDate.now().getYear());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("expertResults", expertResults);
		model.addAttribute("name", name);
		model.addAttribute("examNo", examNo);
		model.addAttribute("expertName", expertName);
		return "result/eresults";
	}

	@GetMapping(value = "/big_deviation_results")
	@RequiredPrivilege(Role.MANAGER)
	public String getBigDeviationResults(Model model) {
		List<ExpertResult> results = this.expertResultService.getBigDeviationResults();
		model.addAttribute("results", results);
		return "result/bresults";
	}

	@GetMapping(value = "/difference_results")
	@RequiredPrivilege(Role.MANAGER)
	public String getDiffResults(Model model) {
		List<ExpertResult> results = this.expertResultService.getDiffResults();
		model.addAttribute("results", results);
		return "result/dresults";
	}

}
