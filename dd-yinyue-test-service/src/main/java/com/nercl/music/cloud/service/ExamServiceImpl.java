package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.nercl.music.cloud.dao.ExamDao;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.Exam.ExamStatus;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

	@Autowired
	private ExamDao examDao;

	@Autowired
	private ExamPaperService examPaperService;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Exam> list(String grade, ExamType examType, String schoolId) {
		return examDao.list(grade, examType, schoolId);
	}

	@Override
	public List<Exam> list(String sid) {
		return examDao.list(sid);
	}

	@Override
	public List<Exam> list(String sid, String gcode, long start, long end) {
		return examDao.list(sid, gcode, start, end);
	}

	@Override
	public String save(Exam exam) {
		examDao.save(exam);
		return exam.getId();
	}

	@Override
	public boolean update(String id, String title, String intro) {
		Exam exam = examDao.findByID(id);
		if (null == exam) {
			return false;
		}
		exam.setTitle(title);
		exam.setIntro(intro);
		examDao.update(exam);
		return true;
	}

	@Override
	public boolean delete(String id) {
		List<ExamPaper> examPapers = examPaperService.getAllByExam(id);
		if (null == examPapers || examPapers.isEmpty()) {
			examDao.deleteById(id);
			return null == examDao.findByID(id);
		}
		examPapers.forEach(examPaper -> examPaperService.delete(examPaper.getId()));
		examDao.deleteById(id);
		return null == examDao.findByID(id);
	}

	@Override
	public Exam findById(String eid) {
		return examDao.findByID(eid);
	}

	@Override
	public List<Exam> searchExams(String schoolId, String matchString) {
		return examDao.searchExams(schoolId, matchString);
	}

	@Override
	public void update(Exam exam) {
		examDao.update(exam);
	}

	@Override
	public boolean setExamValid(String eid) {
		Exam exam = findById(eid);
		if (null == exam) {
			return false;
		}
		exam.setExamStatus(ExamStatus.VALID);
		examDao.update(exam);
		List<Exam> exams = examDao.getExams(exam.getSchoolId(), exam.getGrade());
		if (null != exams) {
			exams.stream().filter(e -> !e.getId().equals(eid) && e.getExamStatus().equals(ExamStatus.VALID))
					.forEach(e -> {
						e.setExamStatus(ExamStatus.EXPIRED);
						examDao.update(e);
					});
		}
		return true;
	}

	@Override
	public List<Exam> getValidExams() {
		return examDao.getValidExams();
	}

	@Override
	public Exam getValidExam(String grade, String schoolId) {
		return examDao.getValidExam(grade, schoolId);
	}

	@Override
	public List<Exam> findByCondition(Exam exam) throws Exception {
		return examDao.findByConditions(exam);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getExamQuestions(String cid, long beginAt, long endAt) {
		List<Exam> exams = examDao.get(beginAt, endAt);
		if (null == exams || exams.isEmpty()) {
			return null;
		}
		Map<String, Object> ret = restTemplate.getForObject(ApiClient.GET_CLASS, Map.class, cid);
		if (null == ret || null == ret.get("gcode") || null == ret.get("sid")) {
			return null;
		}
		String gcode = (String) ret.get("gcode");
		String sid = (String) ret.get("sid");
		List<Question> questions = Lists.newArrayList();
		exams.stream().filter(exam -> sid.equals(exam.getSchoolId()) && gcode.equals(exam.getGrade())).forEach(exam -> {
			List<ExamPaper> papers = examPaperService.getByExam(exam.getId());
			if (null == papers || papers.isEmpty()) {
				return;
			}
			papers.forEach(paper -> {
				questions.addAll(examPaperQuestionService.getQuestionByExamPaper(paper.getId()));
			});
		});
		return questions;
	}

	@Override
	public PaginateSupportArray<Exam> list(ExamType type, String schoolId, String grade, int page, int pageSize) {
		return examDao.list(type, schoolId, grade, page, pageSize);
	}
}
