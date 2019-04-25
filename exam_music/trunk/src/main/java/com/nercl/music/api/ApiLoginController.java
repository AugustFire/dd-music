package com.nercl.music.api;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.nercl.music.constant.CList;
import com.nercl.music.entity.Exam;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Expert;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.AnswerRecordService;
import com.nercl.music.service.ExamPaperService;
import com.nercl.music.service.ExamService;
import com.nercl.music.service.ExamineeService;
import com.nercl.music.service.ExpertService;
import com.nercl.music.service.LoginService;

@RestController
public class ApiLoginController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private LoginService loginService;

	@Autowired
	private ExpertService expertService;

	@Autowired
	private ExamineeService examineeService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamService examService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Value("${exam_music.examinee.photo}")
	private String photoPath;

	@PostMapping(value = "/api/login", produces = JSON_PRODUCES)
	public Map<String, Object> authenticate(String login, String password, Integer subject_type) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户名或者密码为空");
			return ret;
		}
		boolean success = this.loginService.authenticate(login, password);
		if (!success) {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "用户名或者密码错误");
			return ret;
		}
		Examinee examinee = this.examineeService.getByLogin(login);
		if (null == examinee) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "该考生不存在");
			return ret;
		}
		Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考卷不存在");
			return ret;
		}
		ExamPaper examPaper = this.examPaperService.getByExamAndSubjectType(exam.getId(), subject_type);
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "考卷不存在");
			return ret;
		}
		boolean hasanswerRecord = answerRecordService.hasAnswerRecord(exam.getId(), examPaper.getId(),
		        examinee.getId());
		if (hasanswerRecord) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "该考生已提交过答案");
			return ret;
		}
		ret.put("exam_id", exam.getId());
		ret.put("exam_title", exam.getTitle());
		ret.put("exam_paper_id", examPaper.getId());
		ret.put("exam_paper_title", examPaper.getTitle());
		ret.put("code", CList.Api.Client.OK);
		ret.put("id", examinee.getId());
		ret.put("name", examinee.getPerson().getName());
		Login lg = this.loginService.getByLogin(login);
		if (null != lg) {
			ret.put("login", lg.getLogin());
			ret.put("password", lg.getEncryptedPassword());
			ret.put("salt", lg.getSalt());
		}
		ret.put("idcard", examinee.getIdcard());
		ret.put("exam_no", examinee.getExamNo());
		ret.put("gender", examinee.getPerson().getGender().getDesc());
		ret.put("subject_type", subject_type);
		String photo = examinee.getPhoto();
		if (!Strings.isNullOrEmpty(photo)) {
			ret.put("photo", Base64.getEncoder()
			        .encodeToString(Files.toByteArray(new File(this.photoPath + File.separator + photo))));
		}
		String loginToken = loginService.setLoginToken(login);
		ret.put("login_token", loginToken);
		return ret;
	}

	@PostMapping(value = "/api/login/face", produces = JSON_PRODUCES)
	public Map<String, Object> faceAuthenticate(String uuid, String name, Integer subject_type) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(uuid) || StringUtils.isBlank(name)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "uuid或者姓名为空");
			return ret;
		}
		Login login = this.loginService.getByUUIDAndName(uuid, name);
		if (null != login) {
			Examinee examinee = this.examineeService.getByLogin(login.getLogin());
			if (null != examinee) {
				Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
				if (null != exam) {
					ExamPaper examPaper = this.examPaperService.getByExamAndSubjectType(exam.getId(), subject_type);
					if (null != examPaper) {
						ret.put("exam_id", exam.getId());
						ret.put("exam_title", exam.getTitle());
						ret.put("exam_paper_id", examPaper.getId());
						ret.put("exam_paper_title", examPaper.getTitle());
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "考卷不存在");
						return ret;
					}
				} else {
					ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					ret.put("desc", "考试不存在");
					return ret;
				}
				ret.put("code", CList.Api.Client.OK);
				ret.put("id", examinee.getId());
				ret.put("name", examinee.getPerson().getName());
				ret.put("login", login.getLogin());
				ret.put("password", login.getEncryptedPassword());
				ret.put("salt", login.getSalt());
				ret.put("idcard", examinee.getIdcard());
				ret.put("exam_no", examinee.getExamNo());
				ret.put("gender", examinee.getPerson().getGender().getDesc());
				ret.put("subject_type", subject_type);
				String photo = examinee.getPhoto();
				if (!Strings.isNullOrEmpty(photo)) {
					ret.put("photo", Base64.getEncoder()
					        .encodeToString(Files.toByteArray(new File(this.photoPath + File.separator + photo))));
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "该考生不存在");
			}
		} else {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "用户uuid或者姓名错误");
		}
		return ret;
	}

	@PostMapping(value = "/api/expert/login", produces = JSON_PRODUCES)
	public Map<String, Object> expertAuthenticate(String login, String password) throws IOException {
		Map<String, Object> ret = Maps.newHashMap();
		if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "用户名或者密码为空");
			return ret;
		}
		boolean success = this.loginService.authenticate(login, password);
		if (success) {
			Expert expert = this.expertService.getByLogin(login);
			if (null != expert) {
				Exam exam = this.examService.getUsedToExam(LocalDate.now().getYear());
				if (null != exam) {
					ExamPaper examPaper = this.examPaperService.getByExamAndSubjectType(exam.getId(),
					        CList.Api.SubjectType.LOOK_SING);
					if (null != examPaper) {
						ret.put("code", CList.Api.Client.OK);
						ret.put("id", expert.getId());
						ret.put("name", expert.getPerson().getName());
						ret.put("phone", expert.getPerson().getPhone());
						ret.put("gender", expert.getPerson().getGender().getDesc());
						ret.put("job_title", expert.getJobTitle());
						ret.put("unit", expert.getUnit());
						ret.put("intro", expert.getIntro());
						ret.put("exam_id", exam.getId());
						ret.put("exam_title", exam.getTitle());
						ret.put("exam_paper_id", examPaper.getId());
						ret.put("exam_paper_title", examPaper.getTitle());
					} else {
						ret.put("code", CList.Api.Client.PROCESSING_FAILED);
						ret.put("desc", "考卷不存在");
					}
				} else {
					ret.put("code", CList.Api.Client.PROCESSING_FAILED);
					ret.put("desc", "考试不存在");
				}
			} else {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "该专家不存在");
			}
		} else {
			ret.put("code", CList.Api.Client.LOGIN_OR_PASSWORD_ERROR);
			ret.put("desc", "用户名或者密码错误");
		}
		return ret;
	}

}
