package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

public interface AnswerRecordService {

	List<AnswerRecord> get(String classroomId, String qid);

	List<AnswerRecord> getByConditions(AnswerRecord ar) throws Exception;

	AnswerRecord findById(String answerRecordId);

	List<AnswerRecord> get(String userId, long start, long end);

	List<AnswerRecord> get(String userId, String classId, long start, long end);

	List<AnswerRecord> get(String userId, AnswerSource answerSource, long start, long end);

	List<AnswerRecord> get(String userId, AnswerSource answerSource, CompositeAbility ca, String knowledge, long start,
			long end);

	List<AnswerRecord> get(String examPaperId, List<String> cids);

	List<AnswerRecord> getClassRecord(String classId, AnswerSource answerSource, long start, long end);

	List<AnswerRecord> getClassRecord(String classId, String questionId, long start, long end);

	List<AnswerRecord> getClassRecord(String classId, long start, long end);

	List<AnswerRecord> getClassRecord(String cid, String eid);

	List<AnswerRecord> getGradeRecord(String gradeId, String schoolId, long start, long end);

	List<AnswerRecord> getSchoolRecord(String schoolId, long start, long end);

	/**
	 * 记录学生答题信息，其中单选题和多选题自动判断正确与否
	 */
	String save(AnswerRecord answerRecord);

	void update(AnswerRecord answerRecord);

	List<AnswerRecord> getRecords(String uid, long start, long end);

	List<AnswerRecord> getClassRecord(List<String> cids, Long start, Long end);

	boolean setCreationScore(String recordId, Integer fullScore, Integer score);

	List<AnswerRecord> getByGradeAndSchools(String gradeId, String[] sids, Long start, Long end);

	List<AnswerRecord> get(String uid, String examId, String examPaperId);

	List<AnswerRecord> getNoScoreExamRecords();

	boolean hasAnswer(String pid);

	boolean hasAnswer(String pid, String uid);

	Integer getAnswerUserNum(String examPaperId, String classId);

	Integer getAnswerUserNum(String examPaperId, String schoolId, String gradeId);

	Integer getAverageScore(String pid, String classId);

	/**
	 * 根据学校id查询答题人次
	 * 
	 * @param schoolId
	 *            学校id
	 * @param answerSource
	 *            答案来源
	 */
	Integer getAnswerTimesBySchoolId(String schoolId, List<AnswerSource> answerSource);

	/**
	 * 根据学校id查询答题人数
	 * 
	 * @param schoolId
	 *            学校id
	 * @param answerSource
	 *            答案来源
	 */
	Integer getStudentAmountBySchoolId(String schoolId, List<AnswerSource> answerSource);
}
