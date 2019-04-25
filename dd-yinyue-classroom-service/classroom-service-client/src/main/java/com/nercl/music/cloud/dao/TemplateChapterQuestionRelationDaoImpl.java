package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.template.TemplateChapterQuestionRelation;

@Repository
public class TemplateChapterQuestionRelationDaoImpl extends AbstractBaseDaoImpl<TemplateChapterQuestionRelation, String>
		implements TemplateChapterQuestionRelationDao {

	@Override
	public void deleteByTemplate(String templateId) {
		String jpql = "delete from TemplateChapterQuestionRelation crt where crt.templateId = " + templateId;
		this.entityManager.createQuery(jpql).executeUpdate();
	}

	@Override
	public List<TemplateChapterQuestionRelation> getByTemplateChapter(String templateId, String chapterId) {
		String jpql = "from TemplateChapterQuestionRelation crt where crt.templateId = ?1 and crt.chapterId = ?2";
		List<TemplateChapterQuestionRelation> ctqrs = this.executeQueryWithoutPaging(jpql, templateId, chapterId);
		return ctqrs;
	}
}
