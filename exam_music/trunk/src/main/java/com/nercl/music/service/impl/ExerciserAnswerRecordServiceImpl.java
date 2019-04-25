package com.nercl.music.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nercl.music.dao.ExerciserAnswerRecordDao;
import com.nercl.music.entity.ExerciserAnswerRecord;
import com.nercl.music.service.ExamQuestionService;
import com.nercl.music.service.ExerciserAnswerRecordService;

@Service
@Transactional
public class ExerciserAnswerRecordServiceImpl implements ExerciserAnswerRecordService {

	@Autowired
	private ExerciserAnswerRecordDao exerciserAnswerRecordDao;

	@Autowired
	private ExamQuestionService questionService;

	@Override
	public ExerciserAnswerRecord get(String id) {
		return this.exerciserAnswerRecordDao.findByID(id);
	}

	@Override
	public List<ExerciserAnswerRecord> get(String examId, String examPaperId, String exerciserId,
	        String examQuestionId) {
		return this.exerciserAnswerRecordDao.get(examId, examPaperId, exerciserId, examQuestionId);
	}

	@Override
	public ExerciserAnswerRecord save(String examId, String examPaperId, String exerciserId, String examQuestionId,
	        String content, String resPath) {
		ExerciserAnswerRecord record = new ExerciserAnswerRecord();
		if (StringUtils.isNotBlank(examId)) {
			record.setExamId(examId);
		}
		if (StringUtils.isNotBlank(examPaperId)) {
			record.setExamPaperId(examPaperId);
		}
		record.setExerciserId(exerciserId);
		record.setExamQuestionId(examQuestionId);
		record.setCreatAt(System.currentTimeMillis());
		Integer questionType = this.questionService.get(examQuestionId).getQuestionType();
		record.setQuestionType(questionType);
		record.setContent(content);
		record.setResPath(resPath);
		this.exerciserAnswerRecordDao.save(record);
		return StringUtils.isNotBlank(record.getId()) ? record : null;
	}

}
