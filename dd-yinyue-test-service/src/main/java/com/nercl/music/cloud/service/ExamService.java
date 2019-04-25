package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.util.page.PaginateSupportArray;

public interface ExamService {

	String save(Exam exam);

	boolean update(String id, String title, String intro);

	boolean delete(String id);

	List<Exam> list(String grade, ExamType examType, String schoolId);

	List<Exam> list(String sid);

	List<Exam> list(String sid, String gcode, long start, long end);

	/**
	 * 根据Id查询考试
	 */
	Exam findById(String eid);

	/**
	 * 模糊搜索考试
	 * 
	 * @param matchString
	 *            模糊匹配项
	 *            <p>
	 *            模糊匹配项有： 考试名称-title 简介-intro 制作人名-producerName
	 *            </p>
	 */
	List<Exam> searchExams(String matchString, String schoolId);

	/**
	 * 更新考试
	 */
	void update(Exam exam);

	boolean setExamValid(String eid);

	/**
	 * 查询有效的考试
	 */
	List<Exam> getValidExams();

	Exam getValidExam(String grade, String schoolId);

	List<Exam> findByCondition(Exam exm) throws Exception;

	List<Question> getExamQuestions(String cid, long beginAt, long endAt);

	/**
	 * 根据考试类型、学校id、年级代码分页查询考试列表
	 */
	PaginateSupportArray<Exam> list(ExamType type, String schoolId, String grade, int page, int pageSize);

}
