package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.OptionDao;
import com.nercl.music.cloud.entity.Option;

@Service
@Transactional
public class OptionServiceImpl implements OptionService {

	@Autowired
	private OptionDao optionDao;

	@Override
	public void save(Option Option) {
		optionDao.save(Option);
	}

	@Override
	public void delete(Option option) {
		optionDao.delete(option);
	}

	@Override
	public List<Option> getByQuestion(String qid) {
		return optionDao.getByQuestion(qid);
	}

	@Override
	public List<Option> getByGroup(String gid) {
		return optionDao.getByGroup(gid);
	}

	@Override
	public List<Map<String, Object>> list(String qid) {
		List<Option> options = this.optionDao.getByQuestion(qid);
		if (null == options || options.isEmpty()) {
			return null;
		}
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

}
