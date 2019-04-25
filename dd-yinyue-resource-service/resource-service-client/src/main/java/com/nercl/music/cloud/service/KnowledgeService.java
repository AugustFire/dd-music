package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.knowledge.Knowledge;

public interface KnowledgeService {

	List<Knowledge> get(String pid);

	boolean save(String no, String title, Float difficulty, String parentId);

	boolean update(String kid, String no, String title, Float difficulty, String parentId);

	boolean delete(String kid);

}
