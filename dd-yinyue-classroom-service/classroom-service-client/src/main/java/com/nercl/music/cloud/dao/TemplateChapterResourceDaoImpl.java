package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.template.TemplateChapterResource;

@Repository
public class TemplateChapterResourceDaoImpl extends AbstractBaseDaoImpl<TemplateChapterResource, String>
		implements TemplateChapterResourceDao {

	@Override
	public List<TemplateChapterResource> getByChapter(String cid) {
		String jpql = "from TemplateChapterResource crt where crt.chapterId = ?1";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, cid);
		return chapterResourceTemplates;
	}

	@Override
	public List<TemplateChapterResource> getByChapters(List<String> cids) {
		if (cids.size() == 1) {
			String jpql = "from TemplateChapterResource crt where crt.chapterId = ?1";
			List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, cids.get(0));
			return chapterResourceTemplates;
		} else {
			String jpql = "from TemplateChapterResource crt where crt.chapterId = ?1";
			for (int i = 1; i < cids.size(); i++) {
				jpql = jpql + " or crt.chapterId = ?" + (i + 1);
			}
			List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql,
					cids.toArray());
			return chapterResourceTemplates;
		}
	}

	@Override
	public List<TemplateChapterResource> getByTemplate(String tid) {
		String jpql = "from TemplateChapterResource crt where crt.templateId = ?1";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, tid);
		return chapterResourceTemplates;
	}

	@Override
	public List<TemplateChapterResource> findByTemplate(String tid) {
		String jpql = "from TemplateChapterResource crt where crt.templateId = ?1";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, tid);
		return chapterResourceTemplates;
	}

	@Override
	public TemplateChapterResource find(String chapterId, String songId, String resourceId, String templateId) {
		String jpql = "from TemplateChapterResource crt where crt.chapterId = ?1 and crt.songId = ?2 and crt.resourceId = ?3 and crt.templateId = ?4";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, chapterId, songId,
				resourceId, templateId);
		return null == chapterResourceTemplates || chapterResourceTemplates.isEmpty() ? null
				: chapterResourceTemplates.get(0);
	}

	@Override
	public List<TemplateChapterResource> findByResource(String rid) {
		String jpql = "from TemplateChapterResource crt where crt.resourceId = ?1";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, rid);
		return chapterResourceTemplates;
	}

	@Override
	public List<TemplateChapterResource> get(String tid, String cid) {
		String jpql = "from TemplateChapterResource crt where crt.templateId = ?1 and crt.chapterId = ?2";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, tid, cid);
		return chapterResourceTemplates;
	}

	@Override
	public List<TemplateChapterResource> getByParentChapter(String tid, String cid) {
		String jpql = "from TemplateChapterResource crt where crt.templateId = ?1 and  crt.chapter.parentId = ?2";
		List<TemplateChapterResource> chapterResourceTemplates = this.executeQueryWithoutPaging(jpql, tid, cid);
		return chapterResourceTemplates;
	}

}
