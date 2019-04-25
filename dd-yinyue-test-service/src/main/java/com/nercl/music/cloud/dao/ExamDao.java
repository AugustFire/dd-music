package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.Exam;
import com.nercl.music.cloud.entity.Exam.ExamType;
import com.nercl.music.util.page.PaginateSupportArray;

public interface ExamDao extends BaseDao<Exam, String> {

	List<Exam> list(String grade, ExamType examTpye, String schoolId);

	List<Exam> list(String id);

	List<Exam> list(String sid, String gcode, long start, long end);

	/**
	 * 模糊搜索考试
	 * 
	 * @param schoolId
	 *            学校id
	 * @param matchString
	 *            模糊匹配项
	 *            <p>
	 *            模糊匹配项有： 考试名称-title 简介-intro 制作人名-producerName
	 *            </p>
	 */
	List<Exam> searchExams(String schoolId, String matchString);

	/**
	 * 查询有效的考试
	 */
	List<Exam> getValidExams();

	Exam getValidExam(String grade, String schoolId);

	List<Exam> getExams(String schoolId, String grade);

	List<Exam> get(long beginAt, long endAt);

	PaginateSupportArray<Exam> list(ExamType type, String schoolId, String grade, int page, int pageSize);

}
