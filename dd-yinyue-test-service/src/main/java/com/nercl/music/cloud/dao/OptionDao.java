package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.Option;

public interface OptionDao extends BaseDao<Option, String> {

	List<Option> list();

	Option get(String questionId, String value);

	List<Option> getByQuestion(String questionId);

	List<Option> getByGroup(String gid);

}
