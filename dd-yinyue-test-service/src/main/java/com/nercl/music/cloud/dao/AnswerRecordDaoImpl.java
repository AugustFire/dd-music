package com.nercl.music.cloud.dao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.AnswerRecord;
import com.nercl.music.cloud.entity.AnswerSource;
import com.nercl.music.cloud.entity.CompositeAbility;

@Repository
public class AnswerRecordDaoImpl extends AbstractBaseDaoImpl<AnswerRecord, String> implements AnswerRecordDao {

	@Override
	public List<AnswerRecord> get(String classroomId, String qid) {
		String jpql = "from AnswerRecord ar where ar.classRoomId = ?1 and ar.questionId = ?2";
		//修改了之前缺少classroomId参数的bug
		List<AnswerRecord> answerRecords = this.executeQueryWithoutPaging(jpql,classroomId,qid);
		return answerRecords;
	}

	@Override
	public List<AnswerRecord> get(String userId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.userId = ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		return this.executeQueryWithoutPaging(jpql, userId, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, String classId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.userId = ?1 and ar.classId = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, userId, classId, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, AnswerSource answerSource, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.userId = ?1 and ar.answerSource = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, userId, answerSource, start, end);
	}

	@Override
	public List<AnswerRecord> get(String userId, AnswerSource answerSource, CompositeAbility ca, String knowledge,
			long start, long end) {
		String jpql = "from AnswerRecord ar inner join ar.question q where ar.userId = ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		List<String> params = Lists.newArrayList();
		if (null != answerSource) {
			jpql = jpql + " and ar.answerSource = ?4";
			params.add(answerSource.toString());
		}
		if (null != ca) {
			jpql = jpql + " and q.compositeAbilitys like ?5";
			params.add("%" + ca.toString() + "%");
		}
		if (!Strings.isNullOrEmpty(knowledge)) {
			jpql = jpql + " and q.compositeAbilitys like ?6";
			params.add("%" + knowledge + "%");
		}
		return this.executeQueryWithoutPaging(jpql, userId, start, end, params.toArray());
	}

	@Override
	public List<AnswerRecord> get(String examPaperId, List<String> cids) {
		StringBuilder jpql = new StringBuilder("from AnswerRecord ar where ar.examPaperId = ?1 and (");
		for (int i = 0; i < cids.size(); i++) {
			if (i == 0) {
				jpql.append(" ar.classId = ?" + (i + 2));
			} else {
				jpql.append(" or ar.classId = ?" + (i + 2));
			}
		}
		jpql.append(")");
		return this.executeQueryWithoutPaging(jpql.toString(), examPaperId, cids.toArray());
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, AnswerSource answerSource, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.classId = ?1 and ar.answerSource = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, classId, answerSource, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.classId = ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		return this.executeQueryWithoutPaging(jpql, classId, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(String classId, String questionId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.classId = ?1 and ar.questionId = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, classId, questionId, start, end);
	}

	@Override
	public List<AnswerRecord> getRecords(String uid, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.userId = ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		return this.executeQueryWithoutPaging(jpql, uid, start, end);
	}

	@Override
	public List<AnswerRecord> getClassRecord(List<String> cids, Long start, Long end) {
		String jpql = "from AnswerRecord ar where ar.classId in ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		return this.executeQueryWithoutPaging(jpql, cids, start, end);
	}

	@Override
	public List<AnswerRecord> getGradeRecord(String gradeId, String schoolId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.gradeId = ?1 and ar.schoolId = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, gradeId, schoolId, start, end);
	}

	@Override
	public List<AnswerRecord> getSchoolRecord(String schoolId, long start, long end) {
		String jpql = "from AnswerRecord ar where ar.schoolId = ?1 and ar.timestamp >= ?2 and ar.timestamp <= ?3";
		return this.executeQueryWithoutPaging(jpql, schoolId, start, end);
	}

	@Override
	public List<AnswerRecord> getByGradeAndSchools(String gradeId, String[] sids, Long start, Long end) {
		String jpql = "from AnswerRecord ar where ar.schoolId in ?1 and ar.gradeId = ?2 and ar.timestamp >= ?3 and ar.timestamp <= ?4";
		return this.executeQueryWithoutPaging(jpql, Stream.of(sids).collect(Collectors.toList()), gradeId, start, end);
	}

	@Override
	public List<AnswerRecord> get(String uid, String examId, String examPaperId) {
		String jpql = "from AnswerRecord ar where ar.userId = ?1 and ar.examId = ?2 and ar.examPaperId = ?3";
		return this.executeQueryWithoutPaging(jpql, uid, examId, examPaperId);
	}

	@Override
	public List<AnswerRecord> getNoScoreExamRecords() {
		String jpql = "from AnswerRecord ar where ar.examId is NOT NULL and ar.isTrue is NULL and (ar.score <= ?1 or ar.score is NULL)";
		return this.executeQueryWithoutPaging(jpql, 0F);
	}

	@Override
	public int getAnswerCount(String pid) {
		String jpql = "from AnswerRecord ar where ar.examPaperId = ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, pid);
		return count;
	}

	@Override
	public int getAnswerCount(String pid, String uid) {
		String jpql = "from AnswerRecord ar where ar.examPaperId = ?1 and ar.userId = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, pid, uid);
		return count;
	}

	@Override
	public Integer getAnswerUserNum(String examPaperId, String classId) {
		String jpql = "from AnswerRecord ar where ar.examPaperId = ?1 and ar.classId = ?2";
		int count = this.executeCountQuery("select count(DISTINCT ar.userId) " + jpql, examPaperId, classId);
		return count;
	}

	@Override
	public Integer getAnswerUserNum(String examPaperId, String schoolId, String gradeId) {
		String jpql = "from AnswerRecord ar where ar.examPaperId = ?1 and ar.schoolId = ?2 and ar.gradeId = ?3";
		int count = this.executeCountQuery("select count(DISTINCT ar.userId) " + jpql, examPaperId, schoolId, gradeId);
		return count;
	}

	@Override
	public Integer getTotalScore(String examPaperId, String classId) {
		String jpql = "from AnswerRecord ar where ar.examPaperId = ?1 and ar.classId = ?2";
		int count = this.executeCountQuery("select SUM(ar.score) " + jpql, examPaperId, classId);
		return count;
	}

	@Override
	public List<AnswerRecord> getClassRecord(String cid, String eid) {
		String jpql = "from AnswerRecord ar where ar.classId = ?1 and ar.examPaperId = ?2";
		return this.executeQueryWithoutPaging(jpql, cid, eid);
	}

	@Override
	public Integer getAnswerTimesBySchoolId(String schoolId, List<AnswerSource> answerSource) {
		String jpql = "select count(ar.id) from AnswerRecord ar where ar.schoolId = ?1 and ar.answerSource in ?2";
		int count = this.executeCountQuery(jpql, schoolId, answerSource);
		return count;
	}

	@Override
	public Integer getStudentAmountBySchoolId(String schoolId, List<AnswerSource> answerSource) {
		CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<AnswerRecord> root = query.from(AnswerRecord.class); // from
																	// AnswerRecord
		query.select(criteriaBuilder.countDistinct(root.get("userId"))); // count(distinct(userId))
		Predicate predicate = criteriaBuilder.equal(root.get("schoolId"), schoolId); // schoolId
																						// =
		Predicate predicate1 = root.get("answerSource").in(answerSource); // answerSource
																			// in
																			// ()
		query.where(predicate, predicate1); // where schoolId = and answerSource
											// in ()
		Long singleResult = entityManager.createQuery(query).getSingleResult();
		return singleResult.intValue();
	}

	@Override
	public List<AnswerRecord> getRecordInAnswerSource(String gid, String sid, AnswerSource examType, Long start,
			Long end) {
		String jpql = "from AnswerRecord ar where ar.gradeId = ?1 and ar.schoolId = ?2 and ar.answerSource = ?3 and ar.timestamp >= ?4 and ar.timestamp <= ?5";
		return this.executeQueryWithoutPaging(jpql, gid, sid, examType, start, end);
	}
}
