package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.AnswerRecord;
import com.nercl.music.entity.user.Examinee;

public interface AnswerRecordDao extends BaseDao<AnswerRecord, String> {

	List<AnswerRecord> list(Integer questionType);

	List<AnswerRecord> list(String examineeId, int page);

	List<AnswerRecord> list(String examId, String examPaperId, Integer questionType);

	List<AnswerRecord> list(String name, String examNo, String examId, String examPaperId, Integer questionType,
	        int page);

	List<AnswerRecord> listNoScore(String examId);

	AnswerRecord get(String examId, String examPaperId, String examineeId, String examQuestionId);

	List<AnswerRecord> getByExam(String examId);

	List<AnswerRecord> getByExamAndExaminee(String examId, String examPaperId, String examieeId);

	List<AnswerRecord> getByExamPaper(String examId, String examPaperId);

	List<AnswerRecord> getByQuestion(String examId, String questionId);

	List<AnswerRecord> getByExamAndExaminees(String examId, String examPaperId, List<Examinee> examinees);

	boolean hasAnswerRecord(String examId, String examPaperId, String examineeId);

}
