package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.Option;

@Repository
public class OptionDaoImpl extends AbstractBaseDaoImpl<Option, String> implements OptionDao {

	@Override
	public List<Option> list() {
		String jpql = "from Option op";
		List<Option> options = this.executeQueryWithoutPaging(jpql);
		return options;
	}

	@Override
	public Option get(String questionId, String value) {
		String jpql = "from Option op where op.questionId = ?1 and value = ?2";
		List<Option> options = this.executeQueryWithoutPaging(jpql, questionId, value);
		return null == options || options.isEmpty() ? null : options.get(0);
	}

	@Override
	public List<Option> getByQuestion(String questionId) {
		String jpql = "from Option op where op.questionId = ?1";
		List<Option> options = this.executeQueryWithoutPaging(jpql, questionId);
		return options;
	}

	@Override
	public List<Option> getByGroup(String gid) {
		String jpql = "from Option op where op.groupId = ?1";
		List<Option> options = this.executeQueryWithoutPaging(jpql, gid);
		return options;
	}

}
