package com.nercl.music.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.dao.OptionDao;
import com.nercl.music.entity.question.Option;

@Repository
public class OptionDaoImpl extends AbstractBaseDaoImpl<Option, String> implements OptionDao {

	@Override
	public List<Option> list() {
		String jpql = "from Option op";
		List<Option> options = this.executeQueryWithoutPaging(jpql);
		return options;
	}

	@Override
	public List<Option> list(String questionId) {
		String jpql = "from Option op where op.examQuestionId = ?1";
		List<Option> options = this.executeQueryWithoutPaging(jpql, questionId);
		return options;
	}

	@Override
	public Option get(String questionId, String value) {
		String jpql = "from Option op where op.examQuestionId = ?1 and value = ?2";
		List<Option> options = this.executeQueryWithoutPaging(jpql, questionId, value);
		return null == options || options.isEmpty() ? null : options.get(0);
	}

	@Override
	public List<Option> get(String questionId) {
		String jpql = "from Option op where op.examQuestionId = ?1";
		List<Option> options = this.executeQueryWithoutPaging(jpql, questionId);
		return options;
	}

}
