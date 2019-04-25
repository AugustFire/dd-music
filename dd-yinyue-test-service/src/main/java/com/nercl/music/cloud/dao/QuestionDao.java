package com.nercl.music.cloud.dao;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.SubjectType;
import com.nercl.music.util.page.PaginateSupportArray;

public interface QuestionDao extends BaseDao<Question, String> {

	List<Question> getBytopic(String topicId);

	List<Question> getBySubjectType(Integer subjectType, int page);

	List<Question> list(Integer type, String title, Float difficulty, int page);

	/**
	 * 根据条件查询试题列表
	 * 
	 * @param examQuestion
	 *            试题条件
	 * @param page
	 *            当前页码
	 */
	public List<Question> getQuestionsByCondition(Question question, int page);

	List<Question> getQuestionsByTemplate(String templateId);

	List<Question> getByIds(String[] ids);

	List<Question> random(QuestionType type, String grade, int size, List<String> knowledges);

	List<Question> random(List<QuestionType> types, String grade, int size, String knowledge);

	List<Question> random(QuestionType type, SubjectType subjectType, int size);

	List<Question> random(QuestionType type, int size, List<String> exclude, String grade);

	List<Question> getByKnowledgeGrade(String knowledgeNo, String gradeCode);

	/**
	 * 根据难度系数（高，低）、知识点、年级查询试题列表
	 * 
	 * @param knowledges
	 *            知识点
	 * @param gradeCode
	 *            年级码
	 * @param difficultyLow
	 *            难度系数最低值
	 * @param difficultyHigh
	 *            难度系数最高值
	 * @param subjectType
	 *            科目类型
	 * @param questionTypes
	 *            题目类型
	 * @param title
	 *            题目名称
	 * @param pageNum
	 *            查询页码
	 */
	PaginateSupportArray<Question> getquestionList(Float difficultyLow, Float difficultyHigh, List<String> knowledges,
			String gradeCode, String subjectType, List<QuestionType> questionTypes, String title, int pageNum);

	List<Question> getQuestions(String title, String knowledge, String gradeCode, String songId, String questionType,
			String subjectType, Boolean isChecked, int page);

	boolean hasEnoughQuestions(Integer num, String grade, QuestionType type);

	Integer getQuestionsNum(String grade, String knowledge, List<QuestionType> types);

	Map<String, Integer> getQuestionsNumInEveryQuestionType(String grade);

}
