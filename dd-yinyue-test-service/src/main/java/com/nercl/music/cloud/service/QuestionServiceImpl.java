package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.GroupDao;
import com.nercl.music.cloud.dao.QuestionDao;
import com.nercl.music.cloud.entity.Group;
import com.nercl.music.cloud.entity.Option;
import com.nercl.music.cloud.entity.Question;
import com.nercl.music.cloud.entity.QuestionType;
import com.nercl.music.cloud.entity.SubjectType;
import com.nercl.music.util.QuestionToJsonUtil;
import com.nercl.music.util.page.PaginateSupportArray;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionDao questionDao;

	@Autowired
	private OptionService optionService;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private QuestionParser questionParser;

	@Autowired
	private QuestionToJsonUtil questionToJsonUtil;

	@Override
	public void save(Question question) {
		question.setCommitedTime(System.currentTimeMillis());
		questionDao.save(question);
	}

	@Override
	public List<String> save(String json) {
		List<String> ids = questionParser.parse(json);
		return ids;
	}

	@Override
	public Question get(String id) {
		return questionDao.findByID(id);
	}

	@Override
	public List<Map<String, Object>> getByKnowledgeGrade(String knowledgeId, String gradeCode) {
		List<Question> questions = questionDao.getByKnowledgeGrade(knowledgeId, gradeCode);
		if (null == questions) {
			return null;
		}
		List<Map<String, Object>> ret = questions.stream().map(question -> {
			Map<String, Object> q = Maps.newHashMap();
			q.put("id", question.getId());
			q.put("tilte", question.getTitle());
			return q;
		}).collect(Collectors.toList());
		return ret;
	}

	@Override
	public Map<String, Object> get(String[] ids) {
		Map<String, Object> ret = Maps.newHashMap();
		if (null == ids || ids.length < 1) {
			return null;
		}
		List<Question> questions = questionDao.getByIds(ids);
		if (null == questions || questions.isEmpty()) {
			return null;
		}
		Map<String, List<Question>> groupQuestions = questions.stream()
				.filter(question -> !Strings.isNullOrEmpty(question.getGroupId()))
				.collect(Collectors.groupingBy(it -> it.getGroupId()));

		List<Question> nogroupQuestions = questions.stream()
				.filter(question -> Strings.isNullOrEmpty(question.getGroupId())).collect(Collectors.toList());

		List<Map<String, Object>> nogroups = Lists.newArrayList();
		List<Map<String, Object>> groups = Lists.newArrayList();

		nogroupQuestions.forEach(question -> {
			Map<String, Object> json = questionToJsonUtil.toJosn(question);
			nogroups.add(json);
		});

		groupQuestions.forEach((k, v) -> {
			Map<String, Object> map = Maps.newHashMap();
			groups.add(map);
			map.put("group", k);
			List<Map<String, Object>> qs = Lists.newArrayList();
			map.put("questions", qs);
			v.forEach(vv -> {
				Map<String, Object> json = questionToJsonUtil.toJosn(vv);
				qs.add(json);
			});
			List<Option> options = optionService.getByGroup(k);
			if (null == options || options.isEmpty()) {
				return;
			}
			List<Map<String, Object>> os = Lists.newArrayList();
			map.put("options", os);
			options.forEach(option -> {
				Map<String, Object> o = Maps.newHashMap();
				os.add(o);
				o.put("id", option.getId());
				o.put("option_image", option.getOptionImage());
				o.put("xml_path", option.getXmlPath());
				o.put("content", option.getContent());
				o.put("value", option.getValue());
				o.put("is_true", option.isTrue());
			});
		});

		if (!nogroups.isEmpty()) {
			ret.put("questions", nogroups);
		}
		if (!groups.isEmpty()) {
			ret.put("questiongroups", groups);
		}
		return ret;
	}

	@Override
	public boolean delete(String[] ids) {
		/*
		 * Arrays.stream(ids).forEach(id -> { Answer answer =
		 * answerDao.getByQuestion(id); answerDao.delete(answer);
		 * 
		 * List<Option> options = optionService.getByQuestion(id);
		 * options.forEach(option -> optionService.delete(option));
		 * 
		 * questionDao.deleteById(id); });
		 */
		Arrays.stream(ids).forEach(id -> {
			Question question = get(id);
			if (null == question) {
				return;
			}
			question.setIsDeleted(true);
			questionDao.update(question);
		});
		return true;
	}

	@Override
	public void update(Question examQuestion) {
		questionDao.update(examQuestion);
	}

	@Override
	public boolean saveOption(Option option) {
		optionService.save(option);
		return Strings.isNullOrEmpty(option.getId());
	}

	@Override
	public List<Question> getQuestionsByCondition(Question question, int page) {
		return questionDao.getQuestionsByCondition(question, page);
	}

	@Override
	public void saveGroup(Group group) {
		groupDao.save(group);
	}

	@Override
	public List<Question> random(QuestionType type, String grade, int size, List<String> knowledges) {
		return questionDao.random(type, grade, size, knowledges);
	}

	@Override
	public List<Question> random(List<QuestionType> types, String grade, int size, String knowledge) {
		return questionDao.random(types, grade, size, knowledge);
	}

	@Override
	public List<Question> random(QuestionType type, SubjectType subjectType, int size) {
		return questionDao.random(type, subjectType, size);
	}

	@Override
	public List<Question> random(QuestionType type, int size, List<String> exclude, String grade) {
		return questionDao.random(type, size, exclude, grade);
	}

	@Override
	public PaginateSupportArray<Question> getquestionList(Float difficultyLow, Float difficultyHigh,
			List<String> knowledges, String gradeCode, String subjectType, List<QuestionType> questionTypes,
			String title, int pageNum) {
		return questionDao.getquestionList(difficultyLow, difficultyHigh, knowledges, gradeCode, subjectType,
				questionTypes, title, pageNum);
	}

	@Override
	public List<Question> getQuestions(String title, String knowledge, String gradeCode, String songId,
			String questionType, String subjectType, Boolean isChecked, int page) {
		return questionDao.getQuestions(title, knowledge, gradeCode, songId, questionType, subjectType, isChecked,
				page);
	}

	@Override
	public boolean hasEnoughQuestions(Integer num, String grade, QuestionType type) {
		return questionDao.hasEnoughQuestions(num, grade, type);
	}

	@Override
	public Integer getQuestionsNum(String grade, String knowledge, List<QuestionType> types) {
		return questionDao.getQuestionsNum(grade, knowledge, types);
	}

	@Override
	public boolean checked(String qid) {
		Question question = get(qid);
		if (null == question) {
			return true;
		}
		question.setIsChecked(true);
		questionDao.update(question);
		return true;
	}

	@Override
	public Map<String, Integer> getQuestionsNumInEveryQuestionType(String grade) {
		Map<String, Integer> nums = questionDao.getQuestionsNumInEveryQuestionType(grade);
		Map<String, Integer> map = Maps.newHashMap();
		Arrays.stream(QuestionType.values()).forEach(type -> map.put(String.valueOf(type), 0));
		if (null == nums || nums.isEmpty()) {
			return map;
		}
		map.putAll(nums);
		return map;
	}

}
