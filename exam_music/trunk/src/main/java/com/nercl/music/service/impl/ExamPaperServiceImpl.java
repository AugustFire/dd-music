package com.nercl.music.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExamPaperDao;
import com.nercl.music.dao.impl.AbstractBaseDaoImpl;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.Enrollment;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.entity.user.Login;
import com.nercl.music.service.CheckRecordService;
import com.nercl.music.service.ExamPaperService;

@Service
@Transactional
public class ExamPaperServiceImpl extends AbstractBaseDaoImpl<Enrollment, String> implements ExamPaperService {

	@Autowired
	private ExamPaperDao examPaperDao;

	@Autowired
	private CheckRecordService checkRecordService;

	@Value("${exam.paper.score}")
	private Integer score;

	@Value("${looksing.exam.paper.title}")
	private String looksingExamPaperTitle;

	@Value("${looksing.question.resolvedTime}")
	private Integer resolvedTime;

	@Override
	public void save(String title, Integer year, Integer resolvedTime, Integer subjectType) {
		ExamPaper examPaper = new ExamPaper();
		examPaper.setTitle(title);
		examPaper.setYear(year);
		examPaper.setResolvedTime(resolvedTime);
		examPaper.setSubjectType(subjectType);
		examPaper.setCheckStatus(CheckRecord.Status.FOR_CHECKED);
		this.examPaperDao.save(examPaper);
	}

	@Override
	public void save(ExamPaper examPaper) {
		this.examPaperDao.save(examPaper);
	}

	@Override
	public List<ExamPaper> list(int page) {
		return this.examPaperDao.list(page);
	}

	@Override
	public List<ExamPaper> get(String name, int page) {
		return this.examPaperDao.get(name, page);
	}

	@Override
	public List<ExamPaper> get(int year) {
		return this.examPaperDao.get(year);
	}

	@Override
	public ExamPaper get(String examPaperId) {
		ExamPaper examPaper = this.examPaperDao.get(examPaperId);
		return examPaper;
	}

	@Override
	public ExamPaper getByExamAndSubjectType(String examId, Integer subjectType) {
		return this.examPaperDao.getByExamAndSubjectType(examId, subjectType);
	}

	@Override
	public ExamPaper getBySubjectType(Integer subjectType) {
		return this.examPaperDao.getBySubjectType(subjectType);
	}

	@Override
	public boolean update(String id, String title, Integer year, Integer resolvedTime, Integer subjectType) {
		ExamPaper examPaper = this.examPaperDao.get(id);
		if (null != examPaper) {
			examPaper.setTitle(title);
			examPaper.setYear(year);
			examPaper.setResolvedTime(resolvedTime);
			examPaper.setSubjectType(subjectType);
			this.examPaperDao.update(examPaper);
			return true;
		}
		return false;
	}

	@Override
	public void update(ExamPaper examPaper) {
		if (null != examPaper) {
			this.examPaperDao.update(examPaper);
		}
	}

	@Override
	public List<ExamPaper> getByExam(String examId) {
		return this.examPaperDao.getByExam(examId);
	}

	@Override
	public boolean pass(String pid, Login login) {
		if (!pid.contains(",")) {
			ExamPaper examPaper = this.get(pid);
			if (null != examPaper) {
				examPaper.setCheckStatus(CheckRecord.Status.PASSED);
				this.examPaperDao.update(examPaper);
				CheckRecord checkRecord = checkRecordService.getCheckRecord(login, examPaper, CheckRecord.Status.PASSED,
				        "paper", "");
				checkRecordService.addRecord(checkRecord);
				return true;
			}
		} else {
			String[] toPass = pid.split(",");
			for (String id : toPass) {
				this.pass(id, login);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean unpass(String pid, String reason, Login login) {

		if (!pid.contains(",")) {
			ExamPaper examPaper = this.get(pid);
			if (null != examPaper) {
				examPaper.setCheckStatus(CheckRecord.Status.UN_PASSED);
				examPaper.setUnPassReason(reason);
				this.examPaperDao.update(examPaper);
				CheckRecord checkRecord = checkRecordService.getCheckRecord(login, examPaper,
				        CheckRecord.Status.UN_PASSED, "paper", reason);
				checkRecordService.addRecord(checkRecord);
				return true;
			}
		} else {
			String[] toUnPass = pid.split(",");
			for (String id : toUnPass) {
				this.unpass(id, reason, login);
			}
			return true;
		}
		return false;
	}

	@Override
	public void setCheckStatusDefault() {
		List<ExamPaper> ExamPapers = examPaperDao.list();
		if (CollectionUtils.isNotEmpty(ExamPapers)) {
			for (ExamPaper examPaper : ExamPapers) {
				if (null == examPaper.getCheckStatus()) {
					examPaper.setCheckStatus(CheckRecord.Status.FOR_CHECKED);
					examPaperDao.update(examPaper);
				}
			}

		}
	}
}
