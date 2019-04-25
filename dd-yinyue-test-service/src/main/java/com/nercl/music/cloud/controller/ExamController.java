package com.nercl.music.cloud.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Exam.ExamStatus;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.cloud.entity.ExamClassRoom;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.cloud.service.AnswerRecordService;
import com.nercl.music.cloud.service.ExamClassRoomService;
import com.nercl.music.cloud.service.ExamPaperService;
import com.nercl.music.cloud.service.ExamService;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.util.page.PaginateSupportArray;

@RestController
public class ExamController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ExamService examService;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamClassRoomService examClassRoomService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private RestTemplate restTemplate;

/**
 * 查询所有考试
 * @param grade
 * @param examType
 * @param schoolId
 * @param page
 * @param pageSize
 * @return
 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exams")
	public Map<String, Object> list(String grade, @RequestParam(value = "exam_type", required = false) String examType,
			@RequestParam(value = "school_id") String schoolId, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(schoolId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "schoolId is null");
			return ret;
		}
		ExamType type = null;
		if (!Strings.isNullOrEmpty(examType)) {
			if (!ExamType.IsDefined(examType)) {
				ret.put("code", CList.Api.Client.PROCESSING_FAILED);
				ret.put("desc", "exam_type is error");
				return ret;
			}
			type = ExamType.valueOf(examType);
		}
		PaginateSupportArray<Exam> exams = examService.list(type, schoolId, grade, page, pageSize);
		ret.put("code", CList.Api.Client.OK);
		if (null != exams) {
			List<Map<String, Object>> list = Lists.newArrayList();
			exams.forEach(exam -> {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", exam.getId());
				map.put("title", exam.getTitle());
				map.put("intro", exam.getIntro());
				map.put("examStatus", exam.getExamStatus());
				map.put("exam_type", exam.getExamType());
				map.put("isValid", exam.getExamStatus().equals(ExamStatus.VALID));
				List<ExamPaper> examPapers = examPaperService.getByExam(exam.getId());
				map.put("exam_paper_num", null == examPapers || examPapers.isEmpty() ? 0 : examPapers.size());
				Map<String, Object> grades = restTemplate.getForObject(ApiClient.GET_GRADE, Map.class, exam.getGrade());
				List<Map<String, Object>> gradeList = (List<Map<String, Object>>) grades.get("gradeList");
				if (!gradeList.isEmpty()) {
					map.put("grade", gradeList.get(0).get("name"));
				}
				list.add(map);
			});
			ret.put("page", exams.getPage());
			ret.put("page_size", exams.getPageSize());
			ret.put("total", exams.getTotal());
			ret.put("exams", list);
		}
		return ret;
	}

	@GetMapping(value = "/exam/{eid}")
	public Map<String, Object> findExamById(@PathVariable String eid) {
		Map<String, Object> ret = Maps.newHashMap();
		Exam exam = examService.findById(eid);
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam", exam);
		return ret;
	}

	/**
	 * 模糊搜索
	 * <p>
	 * 模糊匹配项有： 考试名称-title 简介-intro 制作人名-producerName
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exam/search")
	public Map<String, Object> searchExams(@RequestParam(value = "school_id") String schoolId,
			@RequestParam String condition) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Exam> exams = examService.searchExams(schoolId, condition);
		List<Map<String,Object>> listExams = Lists.newArrayList();
		exams.forEach(exam -> {
			Map<String,Object> exm = Maps.newHashMap();
			Map<String, Object> grades = restTemplate.getForObject(ApiClient.GET_GRADE, Map.class, exam.getGrade());
			List<Map<String, Object>> gradeList = (List<Map<String, Object>>) grades.get("gradeList");
			if (!gradeList.isEmpty()) {
				exm.put("grade", (String) gradeList.get(0).get("name"));
			}
			exm.put("id", exam.getId());
			exm.put("create_at", exam.getCreateAt());
			exm.put("start_at", exam.getStartAt());
			exm.put("end_at", exam.getEndAt());
			exm.put("exam_status", exam.getExamStatus());
			exm.put("exam_type", exam.getExamType());
			exm.put("intro", exam.getIntro());
			exm.put("title", exam.getTitle());
			exm.put("producer_id", exam.getProducerId());
			exm.put("producer_name", exam.getProducerName());
			exm.put("month", exam.getMonth());
			exm.put("year", exam.getYear());
			exm.put("school_id", exam.getSchoolId());
			exm.put("chapter_id", exam.getChapterId());
			List<ExamPaper> examPapers = examPaperService.getByExam(exam.getId());
			exm.put("exam_paper_num", null == examPapers || examPapers.isEmpty() ? 0 : examPapers.size());
			listExams.add(exm);
		});
		ret.put("code", CList.Api.Client.OK);
		ret.put("exams", listExams);
		return ret;
	}

	@GetMapping(value = "/exam/valid_exam")
	public Map<String, Object> getValidExam(String gradeCode, String examPaperType, String schoolId, String uid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(gradeCode)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "grade is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(schoolId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "schoolId is null");
			return ret;
		}
		boolean validType = Arrays.stream(ExamPaperType.values()).anyMatch(t -> t.toString().equals(examPaperType));
		if (!validType) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examPaperType is error");
			return ret;
		}
		Exam exam = examService.getValidExam(gradeCode, schoolId);
		if (null == exam) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam is null");
			return ret;
		}
		ExamPaper examPaper = examPaperService.getByExamAndPaperType(exam.getId(),
				ExamPaperType.valueOf(examPaperType));
		if (null == examPaper) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no examPaper be found");
			return ret;
		}
		ret.put("exam_id", exam.getId());
		ret.put("exam_title", exam.getTitle());
		ret.put("exam_type", exam.getExamType());
		ret.put("chapter_id", exam.getChapterId());
		ret.put("exam_paper_id", examPaper.getId());
		ret.put("exam_paper_title", examPaper.getTitle());
		long startAt = exam.getStartAt();
		ret.put("start_at", startAt);
		long endAt = exam.getEndAt();
		ret.put("end_at", endAt);
		long now = System.currentTimeMillis();
		ret.put("in_valid_time", now >= startAt && now <= endAt);
		if (!Strings.isNullOrEmpty(uid)) {
			ret.put("has_answer", answerRecordService.hasAnswer(examPaper.getId(), uid));
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/exam", produces = JSON_PRODUCES)
	public Map<String, Object> add(String title, String intro, String producerId, String producerName, String examType,
			String examStatus, String grade, String chapterId, Long startAt, Long endAt) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (!ExamStatus.isDefined(examStatus)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant:" + examStatus);
			return ret;
		}
		if (!ExamType.IsDefined(examType)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "no enum constant:" + examType);
			return ret;
		}
		Exam exam = new Exam();
		exam.setTitle(title);
		exam.setIntro(intro);
		exam.setGrade(grade);
		exam.setProducerId(producerId);
		exam.setProducerName(producerName);
		exam.setExamStatus(ExamStatus.EXPIRED);
		exam.setYear(LocalDate.now().getYear()); // 默认取创建时的年份
		exam.setMonth(LocalDate.now().getMonthValue()); // 默认取创建时的月份
		exam.setExamType(ExamType.valueOf(examType)); // 考试类型
		exam.setChapterId(chapterId);

		exam.setStartAt(startAt);
		exam.setEndAt(endAt);
		exam.setCreateAt(Instant.now().toEpochMilli()); // 考试创建时间

		Map<String, Object> classUser = restTemplate.getForObject(ApiClient.GET_CLASS_USER, Map.class, producerId);
		if (null != classUser && null != classUser.get("school_id")) {
			String schoolId = (String) classUser.get("school_id");
			exam.setSchoolId(schoolId);
		}

		String examId = examService.save(exam);
		if (Strings.isNullOrEmpty(examId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "qid is null");
		} else {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_id", examId);
		}
		return ret;
	}

	@PutMapping("/exam/{id}")
	public Map<String, Object> edit(@PathVariable String id, String title, String intro) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(title)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		boolean success = examService.update(id, title, intro);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_id", id);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "update error");
		}
		return ret;
	}

	/**
	 * 对于不再使用的考试，调用此接口使失效
	 */
	@PutMapping("/exam/nullify/{id}")
	public Map<String, Object> nullifyExam(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		Exam exam = examService.findById(id);
		if (exam == null) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "exam not exist");
			return ret;
		}
		exam.setExamStatus(ExamStatus.EXPIRED);
		examService.update(exam);
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam_id", id);
		return ret;
	}

	/**
	 * 将考试置为有效
	 */
	@PutMapping("/exam/valid/{id}")
	public Map<String, Object> setExamValid(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(id)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		examService.setExamValid(id);
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam_id", id);
		return ret;
	}

	@DeleteMapping("/exam/{id}")
	public Map<String, Object> delete(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		boolean success = this.examService.delete(id);
		if (success) {
			ret.put("code", CList.Api.Client.OK);
			ret.put("exam_id", id);
		} else {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "delete error");
		}
		return ret;
	}

	/**
	 * 考试绑定课堂
	 * 
	 * @param examId
	 *            考试id
	 * @param classroomId
	 *            课堂id
	 */
	@PostMapping(value = "/exam/{examId}/classroom", produces = JSON_PRODUCES)
	public Map<String, Object> bandingClassroom(@PathVariable String examId, String classroomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(examId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "examId is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classroomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classroomId is null");
			return ret;
		}
		ExamClassRoom ecr = new ExamClassRoom();
		ecr.setExamId(examId);
		ecr.setClassroomId(classroomId);
		String examClassRoomId = examClassRoomService.save(ecr);
		ret.put("code", CList.Api.Client.OK);
		ret.put("exam_classroom_id", examClassRoomId);
		return ret;
	}
}
