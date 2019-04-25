package com.nercl.music.cloud.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.TemplateChapterResourceDao;
import com.nercl.music.cloud.entity.template.TemplateChapterResource;

@Service
@Transactional
public class TemplateChapterResourceServiceImpl implements TemplateChapterResourceService {

	@Autowired
	private TemplateChapterResourceDao templateChapterResourceDao;

	@Override
	public boolean save(String cid, String fid) {
		TemplateChapterResource templateChapterResource = new TemplateChapterResource();
		templateChapterResource.setChapterId(cid);
		templateChapterResource.setResourceId(fid);
		templateChapterResourceDao.save(templateChapterResource);
		return !Strings.isNullOrEmpty(templateChapterResource.getId());
	}

	@Override
	public boolean save(String chapterId, String songId, String resourceId, String templateId) {
		TemplateChapterResource templateChapterResource = find(chapterId, songId, resourceId, templateId);
		if (null != templateChapterResource) {
			templateChapterResourceDao.deleteById(templateChapterResource.getId());
		}
		templateChapterResource = new TemplateChapterResource();
		templateChapterResource.setChapterId(chapterId);
		templateChapterResource.setSongId(songId);
		templateChapterResource.setResourceId(resourceId);
		templateChapterResource.setTemplateId(templateId);
		templateChapterResourceDao.save(templateChapterResource);
		return !Strings.isNullOrEmpty(templateChapterResource.getId());
	}

	@Override
	public boolean save(TemplateChapterResource templateChapterResource) {
		templateChapterResourceDao.save(templateChapterResource);
		return !Strings.isNullOrEmpty(templateChapterResource.getId());
	}

	@Override
	public List<TemplateChapterResource> getByChapter(String cid) {
		return templateChapterResourceDao.getByChapter(cid);
	}

	@Override
	public List<TemplateChapterResource> get(String tid, String cid) {
		return templateChapterResourceDao.get(tid, cid);
	}

	@Override
	public List<TemplateChapterResource> getByParentChapter(String tid, String cid) {
		return templateChapterResourceDao.getByParentChapter(tid, cid);
	}

	@Override
	public List<TemplateChapterResource> getByChapters(List<String> cids) {
		return templateChapterResourceDao.getByChapters(cids);
	}

	@Override
	public void deleteByTemplate(String templateId) {
		List<TemplateChapterResource> tcrs = templateChapterResourceDao.findByTemplate(templateId);
		if (null != tcrs) {
			tcrs.forEach(tcr -> {
				templateChapterResourceDao.deleteById(tcr.getId());
			});
		}
	}

	@Override
	public void delete(String cid) {
		templateChapterResourceDao.deleteById(cid);
	}

	@Override
	public List<TemplateChapterResource> getByTemplate(String tid) {
		return templateChapterResourceDao.getByTemplate(tid);
	}

	@Override
	public TemplateChapterResource find(String chapterId, String songId, String resourceId, String templateId) {
		return templateChapterResourceDao.find(chapterId, songId, resourceId, templateId);
	}

	@Override
	public void updateResource(String oldRid, String newRid) {
		List<TemplateChapterResource> tcrs = templateChapterResourceDao.findByResource(oldRid);
		if (null != tcrs) {
			tcrs.forEach(tcr -> {
				tcr.setResourceId(newRid);
				templateChapterResourceDao.update(tcr);
			});
		}
	}

	@Override
	public List<TemplateChapterResource> findByConditions(TemplateChapterResource tcr) throws Exception {
		return templateChapterResourceDao.findByConditions(tcr);
	}

	@Override
	public void update(TemplateChapterResource templateChapterResource) {
		templateChapterResourceDao.update(templateChapterResource);
	}

}
