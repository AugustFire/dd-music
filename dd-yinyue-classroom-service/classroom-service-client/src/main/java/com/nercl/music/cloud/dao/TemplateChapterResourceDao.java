package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateChapterResource;

public interface TemplateChapterResourceDao extends BaseDao<TemplateChapterResource, String> {

	List<TemplateChapterResource> getByChapter(String cid);

	List<TemplateChapterResource> getByChapters(List<String> cids);

	List<TemplateChapterResource> getByTemplate(String tid);

	List<TemplateChapterResource> findByTemplate(String tid);

	TemplateChapterResource find(String chapterId, String songId, String resourceId, String templateId);

	List<TemplateChapterResource> findByResource(String rid);

	List<TemplateChapterResource> get(String tid, String cid);

	List<TemplateChapterResource> getByParentChapter(String tid, String cid);

}
