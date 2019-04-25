package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.Knowledge;

public interface KnowledgeDao extends BaseDao<Knowledge, String> {

	Knowledge getByNo(String no);

	List<Knowledge> getAll();

	List<Knowledge> getTop();

	List<Knowledge> getChildren(String knowledgeNo);

	List<Knowledge> getKnowledgesByParentId(String parentId);
}
