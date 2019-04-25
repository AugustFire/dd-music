package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExamDao;
import com.nercl.music.entity.Exam;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamDaoImpl extends AbstractBaseDaoImpl<Exam, String> implements ExamDao {

	@Override
	public List<Exam> list(int page) {
		String jpql = "from Exam e";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<Exam> exams = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(exams, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public Exam get(int year) {
		String jpql = "from Exam e where e.year = ?1";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, year);
		return null == exams || exams.isEmpty() ? null : exams.get(0);
	}

	@Override
	public Exam get(String id) {
		String jpql = "from Exam e where e.id = ?1";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, id);
		return null == exams || exams.isEmpty() ? null : exams.get(0);
	}

	@Override
	public List<Exam> getByYear(int year) {
		String jpql = "from Exam e where e.year = ?1";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, year);
		return exams;
	}

	@Override
	public Exam getUsedToExam(int year) {
		String jpql = "from Exam e where e.year = ?1 and e.usedToExam = ?2";
		List<Exam> exams = this.executeQueryWithoutPaging(jpql, year, true);
		return null == exams || exams.isEmpty() ? null : exams.get(0);
	}

}
