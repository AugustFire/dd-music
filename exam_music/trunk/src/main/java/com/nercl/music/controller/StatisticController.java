package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.StatisticService;

@Controller
public class StatisticController {

	@Autowired
	private StatisticService statisticService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private Gson gson;

	@GetMapping("/charts")
	public String charts(@RequestParam(value = "exam_id", required = false) String examId, Model model) {
		return "stat/charts";
	}

	@GetMapping("/exam_stat")
	public String statExam(@RequestParam(value = "exam_id", required = false) String examId, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		Map<String, Object> data = this.statisticService.statExam(exam.getId());
		model.addAttribute("data", data);
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("currentYear", LocalDate.now().getYear());
		this.initExamStatChartsData(model, data);
		return "stat/exam_stat";
	}

	@SuppressWarnings("unchecked")
	private void initExamStatChartsData(Model model, Map<String, Object> data) {
		model.addAttribute("papers", Joiner.on("$").join(data.keySet()));
		List<Integer> lessSixtyData = Lists.newArrayList();
		List<Integer> sixtyData = Lists.newArrayList();
		List<Integer> seventyData = Lists.newArrayList();
		List<Integer> eightyData = Lists.newArrayList();
		List<Integer> ninetyData = Lists.newArrayList();
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			Map<String, Object> dataMap = (Map<String, Object>) entry.getValue();
			lessSixtyData.add((Integer) dataMap.get("less_percent_sixty"));
			sixtyData.add((Integer) dataMap.get("percent_sixty"));
			seventyData.add((Integer) dataMap.get("percent_seventyty"));
			eightyData.add((Integer) dataMap.get("percent_eighty"));
			ninetyData.add((Integer) dataMap.get("percent_ninety"));
		}
		model.addAttribute("lessSixtyData", Joiner.on(",").join(lessSixtyData));
		model.addAttribute("sixtyData", Joiner.on(",").join(sixtyData));
		model.addAttribute("seventyData", Joiner.on(",").join(seventyData));
		model.addAttribute("eightyData", Joiner.on(",").join(eightyData));
		model.addAttribute("ninetyData", Joiner.on(",").join(ninetyData));
	}

	@GetMapping("/exam_paper_stat")
	public String statExamPaper(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_paper_id", required = false) String examPaperId, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		List<ExamPaper> examPapers = this.examPaperService.getByExam(exam.getId());
		if (StringUtils.isBlank(examPaperId)) {
			examPaperId = examPapers.get(0).getId();
		}
		Map<String, Object> data = this.statisticService.statExamPaper(exam.getId(), examPaperId);
		model.addAttribute("data", data);
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("examPapers", examPapers);
		model.addAttribute("examPaperId", examPaperId);
		model.addAttribute("currentYear", LocalDate.now().getYear());
		this.initExamPaperStatChartsData(model, data);
		return "stat/exam_paper_stat";
	}

	@SuppressWarnings("unchecked")
	private void initExamPaperStatChartsData(Model model, Map<String, Object> data) {
		model.addAttribute("titles", Joiner.on("$").join(data.keySet()));
		Set<String> scores = Sets.newHashSet();
		Map<String, Map<String, String>> chartMap = Maps.newHashMap();
		model.addAttribute("chartMap", chartMap);
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			Map<String, Object> value = (Map<String, Object>) entry.getValue();
			List<String> strs = (List<String>) value.get("person_score_no");
			strs.forEach(str -> {
				String[] ss = str.split(":");
				scores.add(ss[0]);
				if (null == chartMap.get(ss[0])) {
					Map<String, String> map = Maps.newHashMap();
					chartMap.put(ss[0], map);
				}
				chartMap.get(ss[0]).put(entry.getKey(), ss[1].replace("个", ""));
			});
		}
		List<String> sortedScores = scores.parallelStream().sorted((score1,
		        score2) -> Integer.valueOf(score1.replace("分", "")) - (Integer.valueOf(score2.replace("分", ""))))
		        .collect(Collectors.toList());
		model.addAttribute("scores", Joiner.on(",").join(sortedScores));
		model.addAttribute("chartMap", gson.toJson(chartMap));
	}

	@GetMapping("/examinee_stat")
	public String statExaminee(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "exam_paper_id", required = false) String examPaperId,
	        @RequestParam(value = "exam_no", required = false) String examNo,
	        @RequestParam(value = "name", required = false) String name, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		List<ExamPaper> examPapers = this.examPaperService.getByExam(exam.getId());
		examPaperId = (StringUtils.isBlank(examPaperId) ? examPapers.get(0).getId() : examPaperId);
		Map<String, Object> data = this.statisticService.statExaminee(exam.getId(), examPaperId);
		model.addAttribute("data", data);
		model.addAttribute("personNo", data.keySet().size());
		model.addAttribute("exams", Lists.newArrayList(exam));
		model.addAttribute("examPapers", examPapers);
		model.addAttribute("examPaperId", examPaperId);
		model.addAttribute("currentYear", LocalDate.now().getYear());
		this.initExamineeStatChartsData(model, data);
		return "stat/examinee_stat";
	}

	@SuppressWarnings("unchecked")
	private void initExamineeStatChartsData(Model model, Map<String, Object> data) {
		model.addAttribute("names", Joiner.on(",").join(data.keySet()));
		Map<String, String> titlesMap = Maps.newHashMap();
		Map<String, Map<String, Object>> chartMap = Maps.newHashMap();
		model.addAttribute("chartMap", chartMap);
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			Map<String, Object> map = Maps.newHashMap();
			chartMap.put(entry.getKey(), map);
			List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
			value.forEach(v -> {
				String id = (String) v.get("id");
				String title = (String) v.get("title");
				if (!titlesMap.containsKey(id)) {
					while (titlesMap.containsValue(title)) {
						title = title + " ";
					}
					titlesMap.put(id, title);
				}
				Integer personSocre = (Integer) v.get("person_socre");
				personSocre = null == personSocre ? 0 : personSocre;
				Integer socre = (Integer) v.get("socre");
				socre = null == socre ? 0 : socre;
				double percent = (double) personSocre / socre * 100;
				map.put(title, percent);
			});
		}
		model.addAttribute("titles", Joiner.on("$").join(titlesMap.values()));
		model.addAttribute("chartMap", gson.toJson(chartMap));
	}
}
