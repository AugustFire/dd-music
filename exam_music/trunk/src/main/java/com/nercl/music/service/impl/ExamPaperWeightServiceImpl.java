package com.nercl.music.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExamPaperWeightDao;
import com.nercl.music.dao.impl.AbstractBaseDaoImpl;
import com.nercl.music.entity.ExamExamPaper;
import com.nercl.music.service.ExamPaperWeightService;

@Service
@Transactional
public class ExamPaperWeightServiceImpl extends AbstractBaseDaoImpl<ExamExamPaper, String>
        implements ExamPaperWeightService {

	@Autowired
	private ExamPaperWeightDao examPaperWeightDao;

	@Override
	public boolean save(String examId, String examPaperId, Integer weight) {
		ExamExamPaper examExamPaper = this.get(examId, examPaperId);
		if (null != examExamPaper) {
			examExamPaper.setWeight(weight);
			this.examPaperWeightDao.update(examExamPaper);
			return true;
		}
		examExamPaper = new ExamExamPaper();
		examExamPaper.setExamId(examId);
		examExamPaper.setExamPaperId(examPaperId);
		examExamPaper.setWeight(weight);
		this.examPaperWeightDao.save(examExamPaper);
		return true;
	}

	@Override
	public ExamExamPaper get(String examId, String examPaperId) {
		return this.examPaperWeightDao.get(examId, examPaperId);
	}

}
