package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamDaoImpl extends AbstractBaseDaoImpl<Exam, String> implements ExamDao {

	@Override
	public List<Exam> list(String grade, ExamType examType, String schoolId) {
		String jpql = "from Exam ex where ex.grade=?1 and ex.examType=?2 and ex.schoolId=?3";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, grade, examType, schoolId);
		return exams;
	}

	@Override
	public List<Exam> list(String sid) {
		String jpql = "from Exam ex where ex.schoolId = ?1";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, sid);
		return exams;
	}

	@Override
	public List<Exam> list(String sid, String gcode, long start, long end) {
		String jpql = "from Exam ex where ex.schoolId = ?1 and ex.grade = ?2 and ex.startAt >= ?3 and ex.endAt <= ?4";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, sid, gcode, start, end);
		return exams;
	}

	@Override
	public List<Exam> searchExams(String schoolId, String matchString) {
		String jpql = "from Exam ex where ex.schoolId=?1  and (ex.title like ?2 or ex.intro like ?3 or ex.producerName like ?4) ";
		matchString = "%" + matchString + "%";
		return this.executeQueryWithoutPaging(jpql, schoolId, matchString, matchString, matchString);
	}

	@Override
	public List<Exam> getValidExams() {
		String jpql = "from Exam ex where ex.examStatus = 'VALID'";
		return this.executeQueryWithoutPaging(jpql);
	}

	@Override
	public Exam getValidExam(String grade, String schoolId) {
		String jpql = "from Exam ex where ex.examStatus = 'VALID' and ex.grade = ?1 and ex.schoolId=?2";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, grade, schoolId);
		return null == exams || exams.isEmpty() ? null : exams.get(0);
	}

	@Override
	public List<Exam> getExams(String schoolId, String grade) {
		String jpql = "from Exam ex where ex.schoolId = ?1 and ex.grade = ?2";
		return this.executeQueryWithoutPaging(jpql, schoolId, grade);
	}

	@Override
	public List<Exam> get(long beginAt, long endAt) {
		String jpql = "from Exam ex where ex.startAt >= ?1 and ex.endAt <= ?2";
		return this.executeQueryWithoutPaging(jpql, beginAt, endAt);
	}

	@Override
	public PaginateSupportArray<Exam> list(ExamType type, String schoolId, String grade, int page, int pageSize) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("from Exam ex where 1=1");
		int i = 0;
		List<Object> params = Lists.newArrayList();
		if (type != null) {
			i++;
			sbf.append(" and ex.examType = ?").append(i);
			params.add(type);
		}
		if (!Strings.isNullOrEmpty(schoolId)) {
			i++;
			sbf.append(" and ex.schoolId = ?").append(i);
			params.add(schoolId);
		}
		if (!Strings.isNullOrEmpty(grade)) {
			i++;
			sbf.append(" and ex.grade = ?").append(i);
			params.add(grade);
		}
		sbf.append(" order by ex.createAt desc");   //
//		sbf.append(" order by ex.grade desc,ex.createAt desc"); // 按开始时间排序
		int count = this.executeCountQuery("select count(*) " + sbf.toString(), params.toArray());
		List<Exam> exams = this.executeQueryWithPaging(sbf.toString(), page, pageSize, params.toArray());
		return PaginateSupportUtil.pagingList(exams, page, pageSize, count);
	}
}
