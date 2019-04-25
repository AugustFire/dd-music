package com.nercl.music.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.dao.KnowledgeMapDao;
import com.nercl.music.entity.KnowledgeMap;
import com.nercl.music.service.KnowledgeMapService;

@Service
@Transactional
public class KnowledgeMapServiceImpl implements KnowledgeMapService {

	@Autowired
	private KnowledgeMapDao knowledgeMapDao;

	@Override
	public boolean save(KnowledgeMap knowledgeMap) {
		knowledgeMapDao.save(knowledgeMap);
		return true;
	}

	@Override
	public KnowledgeMap get(String no) {
		return knowledgeMapDao.getByNo(no);
	}

	@Override
	public Map<String, Object> getAll() {
		KnowledgeMap top = this.getTop();
		Map<String, Object> topMap = setToMap(top);
		List<KnowledgeMap> all = this.knowledgeMapDao.getAll();
		this.setChildren(topMap, top, all);
		return topMap;
	}

	private void setChildren(Map<String, Object> parentMap, KnowledgeMap parent, List<KnowledgeMap> all) {
		List<KnowledgeMap> children = all.stream().filter(e -> parent.getId().equals(e.getParentId()))
		        .sorted((e1, e2) -> e1.getId().compareTo(e2.getId())).collect(Collectors.toList());
		if (null != children && !children.isEmpty()) {
			List<Map<String, Object>> childrenJson = Lists.newArrayList();
			parentMap.put("children", childrenJson);
			children.stream().forEach(child -> {
				Map<String, Object> childMap = this.setToMap(child);
				childrenJson.add(childMap);
				this.setChildren(childMap, child, all);
			});
		}
	}

	private Map<String, Object> setToMap(KnowledgeMap knowledgeMap) {
		Map<String, Object> ret = Maps.newLinkedHashMap();
		ret.put("no", knowledgeMap.getNo());
		ret.put("title", knowledgeMap.getTitle());
		ret.put("difficulty", knowledgeMap.getDifficulty());
		return ret;
	}

	@Override
	public KnowledgeMap getTop() {
		return knowledgeMapDao.getTop();
	}

}
