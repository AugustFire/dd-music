package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.template.TemplateChapterQuestionRelation;

public interface TemplateChapterQuestionRelationDao extends BaseDao<TemplateChapterQuestionRelation, String> {

	void deleteByTemplate(String templateId);

	List<TemplateChapterQuestionRelation> getByTemplateChapter(String templateId, String chapterId);

}
