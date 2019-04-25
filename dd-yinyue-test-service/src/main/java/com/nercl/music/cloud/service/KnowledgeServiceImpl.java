package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.KnowledgeDao;
import com.nercl.music.cloud.entity.Knowledge;

@Service
@Transactional
public class KnowledgeServiceImpl implements KnowledgeService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Override
	public boolean save(Knowledge knowledge) {
		knowledgeDao.save(knowledge);
		return !Strings.isNullOrEmpty(knowledge.getId());
	}

	@Override
	public Knowledge get(String no) {
		return knowledgeDao.getByNo(no);
	}

	@Override
	public List<Map<String, Object>> getAll() {
		List<Knowledge> tops = getTop();
		if (null == tops) {
			return null;
		}
		List<Map<String, Object>> all = Lists.newArrayList();
		List<Knowledge> knowledges = knowledgeDao.getAll();
		tops.forEach(top -> {
			Map<String, Object> topMap = setToMap(top);
			all.add(topMap);
			setChildren(topMap, top, knowledges);
		});
		return all;
	}

	private void setChildren(Map<String, Object> parentMap, Knowledge parent, List<Knowledge> all) {
		List<Knowledge> children = all.stream().filter(e -> parent.getId().equals(e.getParentId()))
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

	private Map<String, Object> setToMap(Knowledge knowledgeMap) {
		Map<String, Object> ret = Maps.newLinkedHashMap();
		ret.put("no", knowledgeMap.getNo());
		ret.put("title", knowledgeMap.getTitle());
		return ret;
	}

	@Override
	public List<Knowledge> getTop() {
		return knowledgeDao.getTop();
	}

	@Override
	public List<Knowledge> getChildren(String knowledgeNo) {
		return knowledgeDao.getChildren(knowledgeNo);
	}

	@Override
	public List<Knowledge> getKnowledgesByParentId(String parentId) {
		return knowledgeDao.getKnowledgesByParentId(parentId);
	}

}
