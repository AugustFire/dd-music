package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

public interface AnswerRecordDao extends BaseDao<AnswerRecord, String> {

	List<AnswerRecord> get(String classroomId, String qid);

	List<AnswerRecord> get(String userId, long start, long end);
	
	List<AnswerRecord> get(String userId, String classId, long start, long end);

	List<AnswerRecord> get(String userId, AnswerSource answerSource, long start, long end);

	List<AnswerRecord> get(String userId, AnswerSource answerSource, CompositeAbility ca, String knowledge, long start,
			long end);

	List<AnswerRecord> get(String examPaperId, List<String> cids);

	List<AnswerRecord> getClassRecord(String classId, AnswerSource answerSource, long start, long end);

	List<AnswerRecord> getClassRecord(String classId, long start, long end);

	List<AnswerRecord> getClassRecord(String classId, String questionId, long start, long end);

	List<AnswerRecord> getRecords(String uid, long start, long end);

	List<AnswerRecord> getClassRecord(List<String> cids, Long start, Long end);

	List<AnswerRecord> getGradeRecord(String gradeId, String schoolId, long start, long end);

	List<AnswerRecord> getSchoolRecord(String schoolId, long start, long end);

	List<AnswerRecord> getByGradeAndSchools(String gradeId, String[] sids, Long start, Long end);

	List<AnswerRecord> get(String uid, String examId, String examPaperId);

	List<AnswerRecord> getClassRecord(String cid, String eid);

	List<AnswerRecord> getNoScoreExamRecords();

	int getAnswerCount(String pid);

	int getAnswerCount(String pid, String uid);

	Integer getAnswerUserNum(String classId, String examPaperId);

	Integer getAnswerUserNum(String examPaperId, String schoolId, String gradeId);

	Integer getTotalScore(String examPaperId, String classId);

	Integer getAnswerTimesBySchoolId(String schoolId, List<AnswerSource> answerSource);

	Integer getStudentAmountBySchoolId(String schoolId, List<AnswerSource> answerSource);

	List<AnswerRecord> getRecordInAnswerSource(String gid, String sid, AnswerSource examType, Long start, Long end);

}
