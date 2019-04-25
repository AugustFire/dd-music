package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.dao.ExamDao;
import com.nercl.music.cloud.dao.ExamPaperDao;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;
import com.nercl.music.constant.ApiClient;
import com.nercl.music.constant.CList;
import com.nercl.music.exception.CommonLogicException;

@Service
@Transactional
public class ExamPaperServiceImpl implements ExamPaperService {

	@Autowired
	private ExamDao examDao;

	@Autowired
	private ExamPaperDao examPaperDao;

	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;

	@Autowired
	private AnswerRecordService answerRecordService;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean save(String title, Integer score, String examId, String producerId, String producerName) {
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setProducerId(producerId);
		examPaper.setProducerName(producerName);
		examPaper.setExamId(examId);
		long now = System.currentTimeMillis();
		examPaper.setCreateAt(now);
		examPaper.setUpdateAt(now);
		examPaperDao.save(examPaper);
		return !Strings.isNullOrEmpty(examPaper.getId());
	}

	@Override
	public boolean update(String id, String title, Integer score, Integer resolvedTime, Float difficulty,
			Integer secretLevel) {
		ExamPaper examPaper = examPaperDao.findByID(id);
		if (null == examPaper) {
			return false;
		}
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setDifficulty(difficulty);
		examPaper.setSecretLevel(secretLevel);
		examPaper.setUpdateAt(System.currentTimeMillis());
		examPaperDao.update(examPaper);
		return true;
	}

	@Override
	public boolean update(ExamPaper examPaper) {
		examPaper.setUpdateAt(System.currentTimeMillis());
		examPaperDao.update(examPaper);
		return true;
	}

	@Override
	public boolean update(String id, String title, Integer score, ExamPaper.Status status, Integer resolvedTime,
			Float difficulty) {
		ExamPaper examPaper = examPaperDao.findByID(id);
		if (null == examPaper) {
			return false;
		}
		examPaper.setTitle(title);
		examPaper.setScore(score);
		examPaper.setStatus(ExamPaper.Status.FINISHED);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setDifficulty(difficulty);
		examPaper.setUpdateAt(System.currentTimeMillis());
		examPaperDao.update(examPaper);
		return true;
	}

	@Override
	public boolean delete(String examPaperId) {
		if (answerRecordService.hasAnswer(examPaperId)) {
			throw new CommonLogicException(CList.Api.Client.PROCESSING_FAILED, "has answer");
		} else {
			examPaperQuestionService.deleteByExamPaper(examPaperId);
			examPaperDao.deleteById(examPaperId);
		}
		return true;
	}

	@Override
	public List<ExamPaper> getByExam(String examId) {
		return examPaperDao.getByExam(examId);
	}

	@Override
	public List<ExamPaper> getAllByExam(String examId) {
		return examPaperDao.getAllByExam(examId);
	}

	@Override
	public ExamPaper getByExamAndPaperType(String examId, ExamPaperType examPaperType) {
		return examPaperDao.getByExamAndPaperType(examId, examPaperType);
	}

	@Override
	public ExamPaper get(String id) {
		return examPaperDao.findByID(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExamPaper> get(String cid, long beginAt, long endAt) {
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
		List<Exam> es = exams.stream().filter(exam -> sid.equals(exam.getSchoolId()) && gcode.equals(exam.getGrade()))
				.collect(Collectors.toList());
		List<ExamPaper> papers = Lists.newArrayList();
		es.forEach(exam -> {
			papers.addAll(examPaperDao.getByExam(exam.getId()));
		});
		return papers;
	}

	@Override
	public String save(ExamPaper examPaper) {
		long now = System.currentTimeMillis();
		examPaper.setCreateAt(now);
		examPaper.setUpdateAt(now);
		examPaperDao.save(examPaper);
		return examPaper.getId();
	}

	@Override
	public List<ExamPaper> getExamPapersByUserId(String uid) {
		return examPaperDao.getExamPapersByUserId(uid);
	}

	@Override
	public ExamPaper getNonFinished(String eid) {
		return examPaperDao.getNonFinished(eid);
	}

	@Override
	public boolean clear(String id) {
		ExamPaper examPaper = examPaperDao.findByID(id);
		if (null == examPaper) {
			return false;
		}
		examPaper.setTitle(null);
		examPaper.setScore(null);
		examPaper.setQuestionNum(null);
		examPaper.setUpdateAt(System.currentTimeMillis());
		examPaperDao.update(examPaper);
		examPaperQuestionService.deleteByExamPaper(id);
		return true;
	}

}
