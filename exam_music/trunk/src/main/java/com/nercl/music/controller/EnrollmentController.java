package com.nercl.music.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nercl.music.entity.Enrollment;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.Result;
import com.nercl.music.entity.TimeDuration;
import com.nercl.music.entity.UnPassedReason;
import com.nercl.music.entity.WorkDuration.WorkUnit;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.EnrollmentService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ResultService;

@Controller
public class EnrollmentController implements MessageController {

	@Autowired
	private EnrollmentService enrollmentService;

	@Autowired
	private ExamService examService;

	@Autowired
	private ResultService resultService;
	
	@Value("${exam_music.examinee.photo}")
	private String photoPath;

	/**
	 * 报名列表页
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/enrollments")
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		if (null != exam) {
			List<Enrollment> enrollments = this.enrollmentService.list(exam.getId(), page);
			model.addAttribute("exam", exam);
			model.addAttribute("enrollments", enrollments);
			model.addAttribute("unPassReason", UnPassedReason.get());
		}
		return "enrollment/enrollments";
	}

	/**
	 * 多条件查询报名列表
	 * 
	 * @param name
	 * @param page
	 * @param phone
	 * @param school
	 * @param email
	 * @param idcard
	 * @param examNo
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/enrollments", params = { "name", "examPoint", "examRoom", "examNo" })
	public String getByAttributes(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
	        @RequestParam(value = "name") String name, @RequestParam(value = "examPoint") String examPoint,
	        @RequestParam(value = "examRoom") String examRoom, @RequestParam(value = "examNo") String examNo,
	        String status, Model model) {
		List<Enrollment> enrollments = enrollmentService.getByAttributes(name, examPoint, examRoom, examNo, status,
		        page);
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		model.addAttribute("exam", exam);
		model.addAttribute("enrollments", enrollments);
		model.addAttribute("unPassReason", UnPassedReason.get());
		return "enrollment/enrollments";
	}

	@GetMapping(value = "/enrollments", params = { "exam_id" })
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "exam_id") String examId,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Enrollment> enrollments = this.enrollmentService.list(examId, page);
		model.addAttribute("enrollments", enrollments);
		model.addAttribute("unPassReason", UnPassedReason.get());
		return "enrollment/enrollments";
	}

	@GetMapping(value = "/enrollments", params = { "exam_id", "status" })
	@RequiredPrivilege(Role.MANAGER)
	public String list(@RequestParam(value = "exam_id") String examId, @RequestParam(value = "status") String status,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<Enrollment> enrollments = this.enrollmentService.list(examId, Enrollment.Status.valueOf(status), page);
		model.addAttribute("enrollments", enrollments);
		return "enrollment/enrollments";
	}

	@GetMapping(value = "/enrollment", params = { "eid", "exid" })
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public String get(@RequestParam(value = "eid") String eid, @RequestParam(value = "exid") String exid) {
		this.enrollmentService.get(eid, exid);
		return "result";
	}

	/**
	 * 考生报名页面
	 * 
	 */
	@GetMapping("/enrollment")
	@RequiredPrivilege(Role.EXAMINEE)
	public String toJoin(HttpSession session, Model model) {
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		Examinee exaninee = (Examinee) session.getAttribute("examinee");
		if (null != exam && null != exaninee) {
			Enrollment enrollment = this.enrollmentService.get(exam.getId(), exaninee.getId());
			model.addAttribute("startTime",new Date(exam.getStartAt()));
			model.addAttribute("endTime",new Date(exam.getEndAt()));
			model.addAttribute("enrollment", enrollment);
			String unPassReason = enrollment != null ? enrollment.getUnPassedReason() : "";
			model.addAttribute("unPassReason",unPassReason);
			List<Result> results = resultService.getExamineeResults(exaninee.getId(), exam.getId());
			model.addAttribute("results", results);
		}
		model.addAttribute("exam", exam);
		return "examinee/enrollment";
	}

	/**
	 * 提交报名
	 * 
	 * @param examineeId
	 * @param examId
	 * @return
	 */
	@PostMapping(value = "/enrollment", params = { "examinee_id", "exam_id" })
	@RequiredPrivilege(Role.EXAMINEE)
	@TimeDuration(WorkUnit.ENROLLMENT)
	public String join(@RequestParam(value = "examinee_id") String examineeId,
	        @RequestParam(value = "exam_id") String examId) {
		boolean success = this.enrollmentService.join(examineeId, examId);
		if (success) {
			return "redirect:/enrollment";
		}
		return "redirect:/enrollment";
	}

	/**
	 * 通过报名
	 * 
	 * @param enrollmentId
	 * @return
	 */
	@GetMapping(value = "/enrollment/{enrollmentId}/pass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean pass(@PathVariable String enrollmentId) {
		return this.enrollmentService.pass(enrollmentId);
	}

	/**
	 * 报名不通过
	 * 
	 * @param enrollmentId
	 * @param reason
	 * @return
	 */
	@GetMapping(value = "/enrollment/{enrollmentId}/unpass")
	@ResponseBody
	@RequiredPrivilege(Role.MANAGER)
	public boolean unpass(@PathVariable String enrollmentId, String reason) {
		return this.enrollmentService.unpass(enrollmentId, reason);
	}

}
