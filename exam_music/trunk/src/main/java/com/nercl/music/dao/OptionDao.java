package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.Option;

public interface OptionDao extends BaseDao<Option, String> {

	List<Option> list();

	List<Option> list(String questionId);

	Option get(String questionId, String value);

	List<Option> get(String questionId);

}
