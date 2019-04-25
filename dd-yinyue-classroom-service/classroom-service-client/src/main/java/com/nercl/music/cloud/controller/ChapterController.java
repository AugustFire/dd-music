package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.template.Template;
import com.nercl.music.cloud.entity.template.TemplateParentChapterBookClassRoom;
import com.nercl.music.cloud.service.BookService;
import com.nercl.music.cloud.service.ChapterClassRoomService;
import com.nercl.music.cloud.service.ChapterService;
import com.nercl.music.cloud.service.TemplateParentChapterBookClassRoomService;
import com.nercl.music.constant.CList;

@RestController
public class ChapterController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private TemplateParentChapterBookClassRoomService templateParentChapterBookClassRoomService;

	@Autowired
	private BookService bookService;

	@Autowired
	private ChapterClassRoomService chapterClassRoomService;

	@PostMapping(value = "/chapter/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> getChapterResources(@PathVariable String cid, String classRoomId) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(cid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cid is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(classRoomId)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "classRoomId is null");
			return ret;
		}
		Chapter chapter = chapterClassRoomService.getChapter(classRoomId, cid);
		if (null == chapter) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapter is null");
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<TemplateParentChapterBookClassRoom> classRoomTemplate = templateParentChapterBookClassRoomService
				.getClassRoomTemplate(classRoomId, cid);
		List<Template> templates = Lists.newArrayList();
		if (null != classRoomTemplate) {
			templates = classRoomTemplate.stream().map(TemplateParentChapterBookClassRoom::getTemplate)
					.collect(Collectors.toList());
		}
		ret.put("templates", templates);
		return ret;
	}

	/**
	 * 新增章节
	 * 
	 * @param bid
	 *            教材Id
	 * @param newChapter
	 *            章节
	 */
	@PostMapping(value = "/chapter", produces = JSON_PRODUCES)
	public Map<String, Object> save(Chapter newChapter) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(newChapter.getTitle())) { // 章节标题必输
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (null == newChapter.getOrderBy()) { // 章节顺序必输
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "orderBy is null");
			return ret;
		}
		Book book = bookService.findById(newChapter.getBookId());
		if (null == book) { // 章节对应的教材一定要存在
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "book is not exist");
			return ret;
		}
		newChapter.setBook(book);
		chapterService.save(newChapter);
		ret.put("code", CList.Api.Client.OK);
		ret.put("chapter", newChapter);
		return ret;
	}

	/**
	 * 删除章节及其子章节及章节绑定的资源
	 * 
	 * @param cid
	 *            章节Id
	 */
	@DeleteMapping(value = "/chapter/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String cid) {
		Map<String, Object> ret = Maps.newHashMap();
		Chapter chapter = chapterService.get(cid);
		if (null == chapter) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapter is not exist");
			return ret;
		}
		chapterService.deleteChapterWithChildren(chapter.getId());
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 修改章节
	 * 
	 * @param cid
	 *            章节Id
	 * @param newChapter
	 *            章节
	 */
	@PutMapping(value = "/chapter/{cid}", produces = JSON_PRODUCES)
	public Map<String, Object> update(@PathVariable String cid, Chapter newChapter) {
		Map<String, Object> ret = Maps.newHashMap();
		Chapter chapter = chapterService.get(cid);
		if (null == chapter) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "chapter is not exist");
			return ret;
		}
		if (!Strings.isNullOrEmpty(newChapter.getTitle())) { // 修改title
			chapter.setTitle(newChapter.getTitle());
		}
		if (null != newChapter.getOrderBy()) { // 修改order
			chapter.setOrderBy(newChapter.getOrderBy());
		}
		chapterService.update(chapter);
		ret.put("code", CList.Api.Client.OK);
		chapter.setChildren(null);
		chapter.setParent(null);
		ret.put("chapter", chapter);
		return ret;
	}

	@GetMapping(value = "/all_chapters", produces = JSON_PRODUCES)
	public Map<String, Object> getChapters(@RequestParam("book_id") String bookId) {
		Map<String, Object> ret = Maps.newHashMap();
		List<Map<String, Object>> chapters = chapterService.getChapters(bookId);
		ret.put("code", CList.Api.Client.OK);
		ret.put("chapters", chapters);
		return ret;
	}

	@GetMapping(value = "/chapter/{id}", produces = JSON_PRODUCES)
	public Map<String, Object> getChapter(@PathVariable String id) {
		Map<String, Object> ret = Maps.newHashMap();
		Chapter chapter = chapterService.get(id);
		ret.put("code", CList.Api.Client.OK);
		if (null == chapter) {
			return ret;
		}
		ret.put("id", chapter.getId());
		ret.put("title", chapter.getTitle());
		return ret;
	}

}
