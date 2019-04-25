package com.nercl.music.service;

import java.util.Map;

import com.nercl.music.entity.KnowledgeMap;

public interface KnowledgeMapService {

	boolean save(KnowledgeMap knowledgeMap);

	KnowledgeMap get(String no);

	Map<String, Object> getAll();
	
	KnowledgeMap getTop();

}
