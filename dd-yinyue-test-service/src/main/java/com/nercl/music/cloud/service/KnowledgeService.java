package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;

import com.nercl.music.cloud.entity.Knowledge;

public interface KnowledgeService {

	boolean save(Knowledge knowledge);

	Knowledge get(String no);

	List<Map<String, Object>> getAll();
	
	List<Knowledge> getTop();

	/**
	 * 根据知识点编号查询其下的二级知识点
	 * */
	List<Knowledge> getChildren(String knowledgeNo);

	/**
	 * 根据parentId查询下级的知识点
	 * 
	 * @param parentId
	 */
	List<Knowledge> getKnowledgesByParentId(String parentId);
}
