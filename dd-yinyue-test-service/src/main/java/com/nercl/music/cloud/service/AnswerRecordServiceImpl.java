package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nercl.music.cloud.dao.AnswerRecordDao;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

@Service
@Transactional
public class AnswerRecordServiceImpl implements AnswerRecordService {

	@Autowired
	private AnswerRecordDao answerRecordDao;

	@Override
	public List<AnswerRecord> get(String classroomId, String qid) {
		return answerRecordDao.get(classroomId, qid);
	}

	@Override
	public List<AnswerRecord> getByConditions(AnswerRecord ar) throws Exception {
		return answerRecordDao.findByConditions(ar);
	}

	@Override
	public AnswerRecord findById(String answerRecordId) {
		return answerRecordDao.findByID(answerRecordId);
	}

	@Override
	public List<AnswerRecord> get(String userId, long start, long end) {
		return answerRecordDao.get(userId, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, String classId, long start, long end) {
		return answerRecordDao.get(userId, classId, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, AnswerSource answerSource, long start, long end) {
		if (null == answerSource) {
			return answerRecordDao.get(userId, start, end);
		}
		return answerRecordDao.get(userId, answerSource, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, AnswerSource answerSource, CompositeAbility ca, String knowledge,
			long start, long end) {
		return answerRecordDao.get(userId, answerSource, ca, knowledge, start, end);
	}

	@Override
	public List<AnswerRecord> get(String examPaperId, List<String> cids) {
		return answerRecordDao.get(examPaperId, cids);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, AnswerSource answerSource, long start, long end) {
		if (null == answerSource) {
			return answerRecordDao.getClassRecord(classId, start, end);
		}
		return answerRecordDao.getClassRecord(classId, answerSource, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, long start, long end) {
		return answerRecordDao.getClassRecord(classId, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, String questionId, long start, long end) {
		return answerRecordDao.getClassRecord(classId, questionId, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String cid, String eid) {
		return answerRecordDao.getClassRecord(cid, eid);
	}

	@Override
	public List<AnswerRecord> getGradeRecord(String gradeId, String schoolId, long start, long end) {
		return answerRecordDao.getGradeRecord(gradeId, schoolId, start, end);
	}

	@Override
	public List<AnswerRecord> getSchoolRecord(String schoolId, long start, long end) {
		return answerRecordDao.getSchoolRecord(schoolId, start, end);
	}

	@Override
	public String save(AnswerRecord answerRecord) {
		answerRecordDao.save(answerRecord);
		return answerRecord.getId();
	}

	@Override
	public void update(AnswerRecord answerRecord) {
		answerRecordDao.update(answerRecord);
	}

	@Override
	public List<AnswerRecord> getRecords(String uid, long start, long end) {
		return answerRecordDao.getRecords(uid, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(List<String> cids, Long start, Long end) {
		return answerRecordDao.getClassRecord(cids, start, end);
	}

	@Override
	public boolean setCreationScore(String recordId, Integer fullScore, Integer score) {
		AnswerRecord record = findById(recordId);
		if (null == record) {
			return false;
		}
		record.setFullScore(new Float(String.valueOf(fullScore)));
		record.setScore(new Float(String.valueOf(score)));
		update(record);
		return true;
	}

	@Override
	public List<AnswerRecord> getByGradeAndSchools(String gradeId, String[] sids, Long start, Long end) {
		return answerRecordDao.getByGradeAndSchools(gradeId, sids, start, end);
	}

	@Override
	public List<AnswerRecord> get(String uid, String examId, String examPaperId) {
		return answerRecordDao.get(uid, examId, examPaperId);
	}

	@Override
	public List<AnswerRecord> getNoScoreExamRecords() {
		return answerRecordDao.getNoScoreExamRecords();
	}

	@Override
	public boolean hasAnswer(String pid) {
		int count = answerRecordDao.getAnswerCount(pid);
		return count > 0;
	}

	@Override
	public boolean hasAnswer(String pid, String uid) {
		int count = answerRecordDao.getAnswerCount(pid, uid);
		return count > 0;
	}

	@Override
	public Integer getAnswerUserNum(String examPaperId, String classId) {
		return answerRecordDao.getAnswerUserNum(examPaperId, classId);
	}

	@Override
	public Integer getAnswerUserNum(String examPaperId, String schoolId, String gradeId) {
		return answerRecordDao.getAnswerUserNum(examPaperId, schoolId, gradeId);
	}

	@Override
	public Integer getAverageScore(String examPaperId, String classId) {
		Integer num = getAnswerUserNum(examPaperId, classId);
		Integer score = answerRecordDao.getTotalScore(examPaperId, classId);
		return num <= 0 ? 0 : score / num;
	}

	@Override
	public Integer getAnswerTimesBySchoolId(String schoolId, List<AnswerSource> answerSource) {
		return answerRecordDao.getAnswerTimesBySchoolId(schoolId, answerSource);
	}

	@Override
	public Integer getStudentAmountBySchoolId(String schoolId, List<AnswerSource> answerSource) {
		return answerRecordDao.getStudentAmountBySchoolId(schoolId, answerSource);
	}

}
