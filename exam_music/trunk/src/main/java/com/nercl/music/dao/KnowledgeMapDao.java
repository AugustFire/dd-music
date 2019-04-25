package com.nercl.music.dao;

import java.util.List;

import com.nercl.music.entity.KnowledgeMap;

public interface KnowledgeMapDao extends BaseDao<KnowledgeMap, String> {

	KnowledgeMap getByNo(String no);

	List<KnowledgeMap> getAll();

	KnowledgeMap getTop();
}
