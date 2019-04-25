package com.nercl.music.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.nercl.music.dao.OptionDao;
import com.nercl.music.entity.question.Option;
import com.nercl.music.service.OptionService;

@Service
@Transactional
public class OptionServiceImpl implements OptionService {

	@Autowired
	private OptionDao optionDao;

	@Override
	public List<Option> list() {
		return this.optionDao.list();
	}

	@Override
	public List<Map<String, Object>> list(String questionId) {
		List<Option> options = this.optionDao.list(questionId);
		List<Map<String, Object>> ret = options.stream().map(option -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("option_image", option.getOptionImage());
			map.put("xml_path", option.getXmlPath());
			map.put("content", option.getContent());
			map.put("value", option.getValue());
			map.put("is_true", option.isTrue());
			return map;
		}).collect(Collectors.toList());
		return ret;
	}

	@Override
	public boolean save(Option option) {
		this.optionDao.save(option);
		return true;
	}

	@Override
	public Option get(String questionId, String value) {
		return this.optionDao.get(questionId, value);
	}

	@Override
	public List<Option> get(String questionId) {
		return this.optionDao.get(questionId);
	}

	@Override
	public boolean update(Option option) {
		this.optionDao.update(option);
		return true;
	}

}
