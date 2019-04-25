package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.ExamPaper;
import com.nercl.music.cloud.entity.ExamPaperType;

@Repository
public class ExamPaperDaoImpl extends AbstractBaseDaoImpl<ExamPaper, String> implements ExamPaperDao {

	@Override
	public List<ExamPaper> getByExam(String examId) {
		String jpql = "from ExamPaper ep where ep.examId = ?1 and (ep.status = ?2 or ep.status is NULL)";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examId, ExamPaper.Status.FINISHED);
		return examPapers;
	}

	@Override
	public List<ExamPaper> getAllByExam(String examId) {
		String jpql = "from ExamPaper ep where ep.examId = ?1";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examId);
		return examPapers;
	}

	@Override
	public ExamPaper getByExamAndPaperType(String examId, ExamPaperType examPaperType) {
		String jpql = "from ExamPaper ep where ep.examId = ?1 and ep.examPaperType = ?2 and (ep.status = ?3 or ep.status is NULL)";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, examId, examPaperType,
				ExamPaper.Status.FINISHED);
		return null == examPapers || examPapers.isEmpty() ? null : examPapers.get(0);
	}

	@Override
	public List<ExamPaper> getExamPapersByUserId(String uid) {
		String jpql = "from ExamPaper eep  where eep.producerId = ?1";
		return this.executeQueryWithoutPaging(jpql, uid);
	}

	@Override
	public ExamPaper getNonFinished(String eid) {
		String jpql = "from ExamPaper ep  where ep.examId = ?1 and ep.status = ?2";
		List<ExamPaper> examPapers = this.executeQueryWithoutPaging(jpql, eid, ExamPaper.Status.NON_FINISHED);
		return null == examPapers || examPapers.isEmpty() ? null : examPapers.get(0);
	}

}
