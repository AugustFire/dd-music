package com.nercl.music.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.entity.Option;

public interface OptionService {

	List<Option> list();

	List<Map<String, Object>> list(String questionId);

	Option get(String questionId, String value);

	List<Option> get(String questionId);

	boolean save(Option option);

	boolean update(Option option);

}
