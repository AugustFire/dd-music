package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.Result;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.ExamExamPaperService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExpertResultService;
import com.nercl.music.service.ResultService;

@Controller
public class ResultController {

	@Autowired
	private ResultService resultService;

	@Autowired
	private ExamService examService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private ExamExamPaperService examExamPaperService;

	@Autowired
	private ExpertResultService expertResultService;

	/**
	 * 管理员成绩查询页面
	 */
	@GetMapping(value = "/results")
	@RequiredPrivilege(Role.MANAGER)
	public String results(@RequestParam(value = "exam_id", required = false) String examId,
	        @RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "exam_no", required = false) String examNo,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		if (exam != null) {
			Map<String, Integer> weightMap = this.examExamPaperService.getWeight(exam.getId());
			model.addAttribute("weightMap", weightMap);
			Map<String, Integer> emWeightMap = this.examExamPaperService.getExpertMachineWeight(exam.getId());
			model.addAttribute("emWeightMap", emWeightMap);

			Map<String, List<Result>> resultMap = resultService.list(exam.getId(), name, examNo);
			List<Map<String, Object>> results = Lists.newArrayList();
			for (Map.Entry<String, List<Result>> entry : resultMap.entrySet()) {
				Map<String, Object> scores = Maps.newHashMap();
				results.add(scores);
				List<Result> rs = entry.getValue();
				Examinee examinee = rs.get(0).getExaminee();
				scores.put("name", examinee.getPerson().getName());
				scores.put("exam_no", examinee.getExamNo());
				for (Result r : rs) {
					scores.put(String.valueOf(r.getExamPaper().getSubjectType()), r.getScore());
					if (CList.Api.SubjectType.LOOK_SING.equals(r.getExamPaper().getSubjectType())) {
						scores.put("expert_score", this.expertResultService.getScore(exam.getId(),
						        r.getExamPaper().getId(), examinee.getId()));
					}
				}
				scores.put("looksing_score", this.calculateLooksingScore(emWeightMap, scores));
				scores.put("total_score", this.calculateTotalScore(weightMap, scores));
			}
			model.addAttribute("currentYear", LocalDate.now().getYear());
			model.addAttribute("exams", Lists.newArrayList(exam));
			model.addAttribute("results", results);

		}
		return "result/results";
	}

	private Integer calculateLooksingScore(Map<String, Integer> emWeightMap, Map<String, Object> scores) {
		Integer eweight = emWeightMap.get("e_weight");
		eweight = null == eweight ? 0 : eweight;
		Integer mweight = emWeightMap.get("m_weight");
		mweight = null == mweight ? 0 : mweight;
		Integer expertScore = (Integer) scores.get("expert_score");
		expertScore = null == expertScore ? 0 : expertScore;
		Integer machineScore = (Integer) scores.get(String.valueOf(CList.Api.SubjectType.LOOK_SING));
		machineScore = null == machineScore ? 0 : machineScore;
		return expertScore * eweight / 100 + machineScore * mweight / 100;
	}

	private Integer calculateTotalScore(Map<String, Integer> weightMap, Map<String, Object> scores) {
		Integer yueLiWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.YUE_LI));
		yueLiWeight = null == yueLiWeight ? 0 : yueLiWeight;
		Integer tingYinWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.TING_YIN));
		tingYinWeight = null == tingYinWeight ? 0 : tingYinWeight;
		Integer lookSingWeight = weightMap.get(String.valueOf(CList.Api.SubjectType.LOOK_SING));
		lookSingWeight = null == lookSingWeight ? 0 : lookSingWeight;

		Integer yueLiScore = (Integer) scores.get(String.valueOf(CList.Api.SubjectType.YUE_LI));
		yueLiScore = null == yueLiScore ? 0 : yueLiScore;
		Integer tingYinScore = (Integer) scores.get(String.valueOf(CList.Api.SubjectType.TING_YIN));
		tingYinScore = null == tingYinScore ? 0 : tingYinScore;
		Integer looksingScore = (Integer) scores.get("looksing_score");
		looksingScore = null == looksingScore ? 0 : looksingScore;
		return yueLiScore * yueLiWeight / 100 + tingYinScore * tingYinWeight / 100
		        + looksingScore * lookSingWeight / 100;
	}

	/**
	 * 考生查询各科成绩
	 */
	@GetMapping(value = "/result")
	@ResponseBody
	@RequiredPrivilege(Role.EXAMINEE)
	public List<Result> resultByExamPapers(HttpSession session, String examId, String examineeId) {
		Object examinee = session.getAttribute("examinee");
		Examinee user = new Examinee();
		if (examinee != null) {
			user = (Examinee) examinee;
		}
		Exam exam = null;
		if (StringUtils.isBlank(examId)) {
			exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		} else {
			exam = this.examService.get(examId);
		}
		if (exam != null) {
			List<Result> results = resultService.getExamineeResults(user.getId(), exam.getId());
			return results;
		}
		return null;
	}

	@GetMapping(value = "/result/auto_set_score")
	@RequiredPrivilege(Role.MANAGER)
	public String autoSetScore() {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		if (null != exam) {
			this.answerRecordService.autoSetScore(exam.getId());
		}
		return "redirect:/results";
	}

}
