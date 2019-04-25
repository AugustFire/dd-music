package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.ExamPaperDao;
import com.nercl.music.entity.CheckRecord;
import com.nercl.music.entity.ExamPaper;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class ExamPaperDaoImpl extends AbstractBaseDaoImpl<ExamPaper, String> implements ExamPaperDao {

	@Override
	public List<ExamPaper> list(int page) {
		String jpql = "from ExamPaper ep";
		int count = this.executeCountQuery("select count(*) " + jpql);
		List<ExamPaper> examPapers = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE);
		return PaginateSupportUtil.pagingList(examPapers, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamPaper> get(String name, int page) {
		String jpql = "from ExamPaper ep where ep.title like ?1";
		int count = this.executeCountQuery("select count(*) " + jpql, "%" + name + "%");
		List<ExamPaper> examPapers = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, "%" + name + "%");
		return PaginateSupportUtil.pagingList(examPapers, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<ExamPaper> get(int year) {
		String jpql = "from ExamPaper ep where ep.year = ?1 and ep.checkStatus = ?2";
		return this.executeQueryWithoutPaging(jpql, year, CheckRecord.Status.PASSED);
	}

	@Override
	public ExamPaper get(String examPaperId) {
		String jpql = "from ExamPaper ep where ep.id = ?1";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examPaperId);
		return null == examPapers || examPapers.isEmpty() ? null : examPapers.get(0);
	}

	@Override
	public ExamPaper getBySubjectType(Integer subjectType) {
		String jpql = "from ExamPaper ep where ep.subjectType = ?1";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, subjectType);
		return null == examPapers || examPapers.isEmpty() ? null : examPapers.get(0);
	}

	@Override
	public ExamPaper getByExamAndSubjectType(String examId, Integer subjectType) {
		String jpql = "select exp from ExamExamPaper eep inner join eep.examPaper exp where eep.examId = ?1 and exp.subjectType = ?2";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examId, subjectType);
		return null == examPapers || examPapers.isEmpty() ? null : examPapers.get(0);
	}

	@Override
	public List<ExamPaper> getByExam(String examId) {
		String jpql = "select exp from ExamExamPaper eep inner join eep.examPaper exp where eep.examId = ?1";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examId);
		return examPapers;
	}

	@Override
	public List<ExamPaper> list() {
		String jpql = "from ExamExamPaper eep";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql);
		return examPapers;
	}

}
