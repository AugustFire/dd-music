package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.Option;

public interface OptionService {

	void save(Option Option);

	void delete(Option option);

	List<Option> getByQuestion(String qid);

	List<Option> getByGroup(String gid);

	List<Map<String, Object>> list(String qid);
}
