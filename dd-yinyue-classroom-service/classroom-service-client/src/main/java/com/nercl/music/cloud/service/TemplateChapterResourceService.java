package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateChapterResource;

public interface TemplateChapterResourceService {

	boolean save(String cid, String fid);

	boolean save(TemplateChapterResource chapterResource);

	boolean save(String chapterId, String songId, String resourceId, String templateId);

	List<TemplateChapterResource> getByChapter(String cid);

	List<TemplateChapterResource> get(String tid, String cid);
	
	List<TemplateChapterResource> getByParentChapter(String tid, String cid);

	List<TemplateChapterResource> getByChapters(List<String> cids);

	void delete(String cid);

	void deleteByTemplate(String templateId);

	List<TemplateChapterResource> getByTemplate(String tid);

	TemplateChapterResource find(String chapterId, String songId, String resourceId, String templateId);

	void updateResource(String oldRid, String newRid);

	List<TemplateChapterResource> findByConditions(TemplateChapterResource tcr) throws Exception;

	void update(TemplateChapterResource templateChapterResource);

}
