package com.nercl.music.cloud.dao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.SubjectType;
import com.nercl.music.util.page.PaginateSupportArray;
import com.nercl.music.util.page.PaginateSupportUtil;

@Repository
public class QuestionDaoImpl extends AbstractBaseDaoImpl<Question, String> implements QuestionDao {

	static final int DEFAULT_PAGESIZE = 20;

	@Override
	public List<Question> getBytopic(String topicId) {
		String jpql = "select tq.question from TopicQuestion tq where tq.topicId = ?1";
		List<Question> questions = this.executeQueryWithoutPaging(jpql, topicId);
		return questions;
	}

	@Override
	public List<Question> getBySubjectType(Integer subjectType, int page) {
		String jpql = "from Question eq where eq.subjectType = ?1 order by eq.questionType asc";
		int count = this.executeCountQuery("select count(*) " + jpql, subjectType);
		List<Question> questions = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, subjectType);
		return PaginateSupportUtil.pagingList(questions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Question> list(Integer type, String title, Float difficulty, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExamQuestion eq where 1=1");
		if (type != null) {
			jpql.append(" and eq.questionType = ?").append(++paramCount);
			params.add(type);
		}
		if (StringUtils.isNotBlank(title)) {
			jpql.append(" and eq.title like ?").append(++paramCount);
			params.add("%" + title + "%");
		}
		if (difficulty != null) {
			jpql.append(" and eq.difficulty = ?").append(++paramCount);
			params.add(difficulty);
		}
		jpql.append(" order by eq.commitedTime desc");
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Question> examQuestions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
				params.toArray());
		return PaginateSupportUtil.pagingList(examQuestions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Question> getQuestionsByCondition(Question question, int page) {
		List<Object> params = Lists.newArrayList();
		int paramCount = 0;
		StringBuilder jpql = new StringBuilder("from ExamQuestion eq where 1=1");
		// 试题类型
		if (question.getQuestionType() != null) {
			jpql.append(" and eq.questionType = ?").append(++paramCount);
			params.add(question.getQuestionType());
		}
		// 科目类型
		if (question.getSubjectType() != null) {
			jpql.append(" and eq.subjectType = ?").append(++paramCount);
			params.add(question.getSubjectType());
		}
		// 题干 模糊匹配
		if (StringUtils.isNotBlank(question.getTitle())) {
			jpql.append(" and eq.title like ?").append(++paramCount);
			params.add("%" + question.getTitle() + "%");
		}
		// 考点/考试内容
		if (question.getExamField() != null) {
			jpql.append(" and eq.examField = ?").append(++paramCount);
			params.add(question.getExamField());
		}
		// 难度系数
		if (question.getDifficulty() != null) {
			jpql.append(" and eq.difficulty = ?").append(++paramCount);
			params.add(question.getDifficulty());
		}
		// 分值
		if (question.getScore() != null) {
			jpql.append(" and eq.score = ?").append(++paramCount);
			params.add(question.getScore());
		}

		jpql.append(" order by eq.commitedTime desc");
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Question> questions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
				params.toArray());
		return PaginateSupportUtil.pagingList(questions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Question> getQuestionsByTemplate(String templateId) {
		String jpql = "from Question eq where eq.templateId = ?1";
		return this.executeQueryWithoutPaging(jpql, templateId);
	}

	@Override
	public List<Question> getByIds(String[] ids) {
		StringBuilder sb = new StringBuilder("from Question qq where");
		for (int k = 0; k < ids.length; k++) {
			if (k > 0) {
				sb.append(" or");
			}
			sb.append(" qq.id = " + "?" + (k + 1));
		}
		return this.executeQueryWithoutPaging(sb.toString(), Arrays.stream(ids).toArray());
	}

	@Override
	public List<Question> random(QuestionType type, String grade, int size, List<String> knowledges) {
		if (null == knowledges || knowledges.isEmpty()) {
			String jpql = "from Question cn where cn.questionType = ?1 and cn.grade = ?2";
			int count = this.executeCountQuery("select count(*) " + jpql, type, grade);
			return this.findRandom(size, count, jpql, type, grade);
		} else {
			List<Object> params = Lists.newArrayList();
			params.add(type);
			params.add(grade);
			StringBuilder jpql = new StringBuilder("from Question cn where cn.questionType = ?1 and cn.grade = ?2");
			for (int i = 0; i < knowledges.size(); i++) {
				if (i == 0) {
					jpql.append(" and (cn.knowledges like ?" + (i + 3));
				} else {
					jpql.append(" or cn.knowledges like ?" + (i + 3));
				}
				if (i == knowledges.size() - 1) {
					jpql.append(")");
				}
				params.add("%" + knowledges.get(i) + "%");
			}
			int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
			return this.findRandom(size, count, jpql.toString(), params.toArray());
		}
	}

	@Override
	public List<Question> random(List<QuestionType> types, String grade, int size, String knowledge) {
		if (null == types || types.isEmpty()) {
			String jpql = "from Question cn where cn.grade = ?1 and cn.knowledges like ?2";
			int count = this.executeCountQuery("select count(*) " + jpql, grade, "%" + knowledge + "%");
			return this.findRandom(size, count, jpql, grade, knowledge);
		} else {
			StringBuilder jpql = new StringBuilder("from Question cn where cn.grade = ?1 and cn.knowledges like ?2");
			List<Object> params = Lists.newArrayList();
			params.add(grade);
			params.add("%" + knowledge + "%");
			for (int i = 0; i < types.size(); i++) {
				if (i == 0) {
					jpql.append(" and (cn.questionType = ?" + (i + 3));
				} else {
					jpql.append(" or cn.questionType = ?" + (i + 3));
				}
				if (i == types.size() - 1) {
					jpql.append(")");
				}
				params.add(types.get(i));
			}
			int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
			return this.findRandom(size, count, jpql.toString(), params.toArray());
		}
	}

	@Override
	public List<Question> random(QuestionType type, SubjectType subjectType, int size) {
		String jpql = "from Question cn where cn.questionType = ?1 and cn.subjectType = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, type, subjectType);
		return this.findRandom(size, count, jpql, type, subjectType);
	}

	@Override
	public List<Question> random(QuestionType type, int size, List<String> exclude, String grade) {
		String jpql = "from Question cn where cn.questionType = ?1 and cn.grade = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, type, grade);
		List<Question> questions = findRandom(size, count, jpql, type, grade);
		if (null == questions || questions.isEmpty()) {
			return null;
		}
		if (null == exclude || exclude.isEmpty()) {
			return questions;
		}
		boolean repeat = questions.stream().anyMatch(question -> exclude.contains(question.getId()));
		while (repeat) {
			questions = findRandom(size, count, jpql, type, grade);
			repeat = questions.stream().anyMatch(question -> exclude.contains(question.getId()));
		}
		return questions;
	}

	@Override
	public List<Question> getByKnowledgeGrade(String knowledgeNo, String gradeCode) {
		String jpql = "from Question cn where cn.title like ?1 or ex.intro like ?2 or ex.producerName like ?3";
		return this.executeQueryWithoutPaging(jpql, knowledgeNo, gradeCode);
	}

	@Override
	public PaginateSupportArray<Question> getquestionList(Float difficultyLow, Float difficultyHigh,
			List<String> knowledges, String gradeCode, String subjectType, List<QuestionType> questionTypes,
			String title, int page) {
		List<Object> params = Lists.newLinkedList();
		int k = 0;
		StringBuffer jpql = new StringBuffer().append("from Question q where isChecked is true "); // 审核状态为通过的
		if (null != difficultyLow) {
			k++;
			jpql.append(" and q.difficulty >= ?").append(k);
			params.add(difficultyLow.floatValue());
		}
		if (null != difficultyHigh) {
			k++;
			jpql.append("  and q.difficulty <= ?").append(k);
			params.add(difficultyHigh.floatValue());
		}
		if (!Strings.isNullOrEmpty(title)) {
			k++;
			jpql.append("  and q.title like ?").append(k);
			params.add("%" + title + "%");
		}
		if (null != questionTypes && !questionTypes.isEmpty()) {
			k++;
			jpql.append("  and q.questionType in ?").append(k);
			params.add(questionTypes);
		}
		if (null != knowledges && !knowledges.isEmpty()) {
			Iterator<String> iterator = knowledges.iterator();
			jpql.append(" and (");
			while (iterator.hasNext()) {
				String knowledge = (String) iterator.next();
				k++;
				jpql.append(" q.knowledges like ?").append(k).append(" or ");
				params.add("%" + knowledge + "%");
			}
			jpql.delete(jpql.lastIndexOf("or "), jpql.lastIndexOf("or ") + 3).append(")");
		}
		if (!Strings.isNullOrEmpty(gradeCode)) {
			k++;
			jpql.append(" and q.grade = ?").append(k);
			params.add(gradeCode);
		}
		if (!Strings.isNullOrEmpty(subjectType)) {
			// 如果试卷类型为"ACT" 则只查询科目类型为"ACT"的试题
			if (subjectType.equals("ACT")) {
				jpql.append(" and q.subjectType = 'ACT'");
			} else { // 否则查询其他科目类型的试题
				jpql.append(" and q.subjectType !=  'ACT'");
			}
		}
		page = (page < 1) ? 1 : page;
		int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
		List<Question> questions = this.executeQueryWithPaging(jpql.toString(), page, DEFAULT_PAGESIZE,
				params.toArray());
		return PaginateSupportUtil.pagingList(questions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public List<Question> getQuestions(String title, String knowledge, String gradeCode, String songId,
			String questionType, String subjectType, Boolean isChecked, int page) {
		String jpql = "from Question q where 1=1";
		List<Object> params = Lists.newArrayList();
		int i = 0;
		if (!Strings.isNullOrEmpty(title)) {
			i++;
			jpql = jpql + " and q.title like ?" + i;
			params.add("%" + title + "%");
		}
		if (!Strings.isNullOrEmpty(knowledge)) {
			i++;
			jpql = jpql + " and q.knowledges like ?" + i;
			params.add("%" + knowledge + "%");
		}
		if (!Strings.isNullOrEmpty(gradeCode)) {
			i++;
			jpql = jpql + " and q.grade = ?" + i;
			params.add(gradeCode);
		}
		if (!Strings.isNullOrEmpty(questionType)) {
			List<String> qTypes = Splitter.on(",").splitToList(questionType);
			if (qTypes.size() == 1) {
				i++;
				jpql = jpql + " and q.questionType = ?" + i;
			} else {
				for (int k = 0; k < qTypes.size(); k++) {
					i++;
					if (k == 0) {
						jpql = jpql + " and (q.questionType = ?" + i;
					} else if (k > 0) {
						jpql = jpql + " or q.questionType = ?" + i;
					}
				}
				jpql = jpql + ")";
			}
			params.addAll(qTypes.stream().map(qType -> QuestionType.valueOf(qType)).collect(Collectors.toList()));
		}
		if (!Strings.isNullOrEmpty(subjectType)) {
			List<String> sTypes = Splitter.on(",").splitToList(subjectType);
			if (sTypes.size() == 1) {
				i++;
				jpql = jpql + " and q.subjectType = ?" + i;
			} else {
				for (int k = 0; k < sTypes.size(); k++) {
					i++;
					if (k == 0) {
						jpql = jpql + " and (q.subjectType = ?" + i;
					} else if (k > 0) {
						jpql = jpql + " or q.subjectType = ?" + i;
					}
				}
				jpql = jpql + ")";
			}
			params.addAll(sTypes.stream().map(sType -> SubjectType.valueOf(sType)).collect(Collectors.toList()));
		}
		if (!Strings.isNullOrEmpty(songId)) {
			i++;
			jpql = jpql + " and (q.songId = ?" + i + " or q.songId is NULL or q.songId ='')";
			params.add(songId);
		}
		if (null != isChecked && isChecked) {
			i++;
			jpql = jpql + " and q.isChecked = ?" + i;
			params.add(true);
		} else {
			i++;
			jpql = jpql + " and (q.isChecked = ?" + i + " or q.isChecked is NULL)";
			params.add(false);
		}
		jpql = jpql + " and (q.isDeleted is NULL or q.isDeleted = false)";
		int count = this.executeCountQuery("select count(*) " + jpql, params.toArray());
		List<Question> questions = this.executeQueryWithPaging(jpql, page, DEFAULT_PAGESIZE, params.toArray());
		return PaginateSupportUtil.pagingList(questions, page, DEFAULT_PAGESIZE, count);
	}

	@Override
	public boolean hasEnoughQuestions(Integer num, String grade, QuestionType type) {
		String jpql = "from Question cn where cn.grade = ?1 and cn.questionType = ?2";
		int count = this.executeCountQuery("select count(*) " + jpql, grade, type);
		return count >= num;
	}

	@Override
	public Integer getQuestionsNum(String grade, String knowledge, List<QuestionType> types) {
		if (null == types || types.isEmpty()) {
			if (Strings.isNullOrEmpty(knowledge)) {
				String jpql = "from Question cn where cn.grade = ?1";
				int count = this.executeCountQuery("select count(*) " + jpql, grade);
				return count;
			} else {
				String jpql = "from Question cn where cn.grade = ?1 and cn.knowledges like ?2";
				int count = this.executeCountQuery("select count(*) " + jpql, grade, "%" + knowledge + "%");
				return count;
			}
		} else {
			if (Strings.isNullOrEmpty(knowledge)) {
				StringBuilder jpql = new StringBuilder("from Question cn where cn.grade = ?1");
				List<Object> params = Lists.newArrayList();
				params.add(grade);
				for (int i = 0; i < types.size(); i++) {
					if (i == 0) {
						jpql.append(" and (cn.questionType = ?" + (i + 2));
					} else {
						jpql.append(" or cn.questionType = ?" + (i + 2));
					}
					if (i == types.size() - 1) {
						jpql.append(")");
					}
					params.add(types.get(i));
				}
				int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
				return count;
			} else {
				StringBuilder jpql = new StringBuilder(
						"from Question cn where cn.grade = ?1 and cn.knowledges like ?2");
				List<Object> params = Lists.newArrayList();
				params.add(grade);
				params.add(knowledge);
				for (int i = 0; i < types.size(); i++) {
					if (i == 0) {
						jpql.append(" and (cn.questionType = ?" + (i + 3));
					} else {
						jpql.append(" or cn.questionType = ?" + (i + 3));
					}
					if (i == types.size() - 1) {
						jpql.append(")");
					}
					params.add(types.get(i));
				}
				int count = this.executeCountQuery("select count(*) " + jpql.toString(), params.toArray());
				return count;
			}
		}
	}

	@Override
	public Map<String, Integer> getQuestionsNumInEveryQuestionType(String grade) {
		String jpql = "select cn.questionType, count(id) as qids from Question cn where cn.grade = ?1 group by cn.questionType";
		List<Object[]> nums = this.executeQuery(jpql, grade);
		if (null == nums || nums.isEmpty()) {
			return null;
		}
		Map<String, Integer> map = Maps.newHashMap();
		nums.forEach(num -> {
			map.put(String.valueOf(num[0]), ((Long) num[1]).intValue());
		});
		return map;
	}
}
