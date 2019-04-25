package com.nercl.music.cloud.service;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.dao.ChapterDao;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.template.TemplateChapterResource;

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {

	@Autowired
	private ChapterDao chapterDao;

	@Autowired
	private TemplateChapterResourceService templateChapterResourceService;

	@Override
	public boolean save(String rid, String title) {
		Chapter chapter = new Chapter();
		chapter.setTitle(title);
		chapterDao.save(chapter);
		return !Strings.isNullOrEmpty(chapter.getId());
	}

	@Override
	public boolean save(Chapter chapter) {
		chapterDao.save(chapter);
		return !Strings.isNullOrEmpty(chapter.getId());
	}

	@Override
	public Chapter get(String id) {
		return chapterDao.findByID(id);
	}

	@Override
	public List<Chapter> getChildrenChapters(String pid) {
		return chapterDao.getChildrenChapters(pid);
	}

	@Override
	public List<Chapter> getByBook(String bid) {
		return chapterDao.getByBook(bid);
	}

	@Override
	public void delete(String cid) {
		chapterDao.deleteById(cid);
	}

	@Override
	public void deleteChapterWithChildren(String id) {
		List<Chapter> chapters = chapterDao.getChildrenChapters(id);
		if (chapters.isEmpty()) { // 如果当前章节没有子章节，则删除章节本身
			// 删除章节绑定的资源
			List<TemplateChapterResource> chapterResources = templateChapterResourceService.getByChapter(id);
			if (null != chapterResources) {
				chapterResources.forEach(cr -> {
					templateChapterResourceService.delete(cr.getId());
				});
			}
			chapterDao.deleteById(id);
		} else {
			// 如果有子章节
			Stack<Chapter> stack = new Stack<Chapter>();
			recursion(id, stack);
			while (!stack.isEmpty()) {
				Chapter chapter = stack.pop();
				List<TemplateChapterResource> chapterResources = templateChapterResourceService
						.getByChapter(chapter.getId());
				chapterResources.forEach(cr -> {
					templateChapterResourceService.delete(cr.getId());
				});
				chapterDao.deleteById(chapter.getId());
			}
		}
	}

	private void recursion(String cid, Stack<Chapter> stack) {
		Chapter chapter = chapterDao.findByID(cid);
		if (null == chapter) {
			return;
		}
		stack.push(chapter);
		List<Chapter> children = chapterDao.getChildrenChapters(cid);
		if (null != children && !children.isEmpty()) {
			children.forEach(child -> {
				recursion(child.getId(), stack);
			});
		}
	}

	@Override
	public void update(Chapter newChapter) {
		chapterDao.update(newChapter);
	}

	@Override
	public List<Chapter> getChapters(String bid, String parentId) {
		return chapterDao.getChapters(bid, parentId);
	}

	@Override
	public List<Map<String, Object>> getChapters(String bid) {
		List<Chapter> chapters = chapterDao.getByBook(bid);
		if (null == chapters) {
			return null;
		}
		List<Chapter> parents = chapters.stream().filter(chapter -> Strings.isNullOrEmpty(chapter.getParentId()))
				.collect(Collectors.toList());
		List<Map<String, Object>> list = Lists.newArrayList();
		parents.forEach(parent -> {
			Map<String, Object> map = Maps.newHashMap();
			map.put("pid", parent.getId());
			map.put("ptitle", parent.getTitle());
			list.add(map);
			List<Chapter> children = parent.getChildren();
			if (null != children) {
				List<Map<String, String>> clist = Lists.newArrayList();
				children.forEach(child -> {
					Map<String, String> cmap = Maps.newHashMap();
					cmap.put("cid", child.getId());
					cmap.put("ctitle", child.getTitle());
					cmap.put("teach_form", String.valueOf(child.getTeachForm()));
					clist.add(cmap);
				});
				map.put("children", clist);
			}
		});
		return list;
	}

	@Override
	public String addSubChapter(String pid, String title) {
		Chapter parent = chapterDao.findByID(pid);
		if (null == parent) {
			return null;
		}
		Chapter chapter = new Chapter();
		chapter.setTitle(title);
		chapter.setParentId(pid);
		chapterDao.save(chapter);
		return chapter.getId();
	}

	@Override
	public Chapter findById(String chapterId) {
		return chapterDao.findByID(chapterId);
	}

}
