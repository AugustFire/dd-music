package com.nercl.music.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.cloud.entity.base.Grade;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.cloud.service.BookService;
import com.nercl.music.cloud.service.GradeService;
import com.nercl.music.constant.CList;

@RestController
public class BookController {

	private static final String JSON_PRODUCES = "application/json;charset=UTF-8";

	@Autowired
	private BookService bookService;

	@Autowired
	private GradeService gradeService;

//	@PostMapping(value = "/init", produces = JSON_PRODUCES)
	public Map<String, Object> init() {
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("code", CList.Api.Client.OK);
		bookService.init();
		return ret;
	}

	/**
	 * 新增教材
	 * 
	 * @param book
	 *            教材
	 */
//	@PostMapping(value = "/book", produces = JSON_PRODUCES)
	public Map<String, Object> save(Book book) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(book.getImgTfileId())) { // 书的封面
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "cover img is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(book.getIsbn())) { // 书的isbn编码
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "isbn is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(book.getTitle())) { // 书名
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "title is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(book.getPublishHouse())) { // 出版社
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "publishing house is null");
			return ret;
		}
		if (Strings.isNullOrEmpty(book.getGradeId())) { // 年级Id
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "grade is null");
			return ret;
		}
		String gradeId = book.getGradeId(); // 年级
		Grade grade = gradeService.findById(gradeId);
		if (null == grade) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "grade is not exist");
			return ret;
		} else {
			book.setGrade(grade);
		}
		bookService.save(book);
		ret.put("code", CList.Api.Client.OK);
		ret.put("book", book);
		return ret;
	}

	/**
	 * 根据条件查询教材
	 * 
	 * @param book
	 *            教材
	 */
	@GetMapping(value = "/book", produces = JSON_PRODUCES)
	public Map<String, Object> getByConditions(Book book) {
		Map<String, Object> ret = Maps.newHashMap();
		try {
			List<Book> bookList = bookService.findByConditions(book);
			ret.put("code", CList.Api.Client.OK);
			ret.put("bookList", bookList);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
		}
		return ret;
	}

	@GetMapping(value = "/book/teach_forms", produces = JSON_PRODUCES)
	public Map<String, Object> getTeachForms(String version) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(version)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "version is null");
			return ret;
		}
		VersionDesc versionDesc = null;
		try {
			versionDesc = VersionDesc.valueOf(version);
		} catch (Exception e) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("code", "not found version: " + version);
			return ret;
		}
		ret.put("code", CList.Api.Client.OK);
		List<TeachForm> teachForms = bookService.getTeachForms(versionDesc);
		if (null != teachForms) {
			List<Map<String, String>> list = Lists.newArrayList();
			teachForms.forEach(tf -> {
				Map<String, String> map = Maps.newHashMap();
				map.put("desc", tf.getDesc());
				map.put("teach_form", String.valueOf(tf));
				list.add(map);
			});
			ret.put("teach_forms", list);
		}
		return ret;
	}

	/**
	 * 根据id删除教材
	 * 
	 * @param id
	 *            教材id
	 */
//	@DeleteMapping(value = "/book/{bid}", produces = JSON_PRODUCES)
	public Map<String, Object> delete(@PathVariable String bid) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(bid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		bookService.deleteById(bid);
		ret.put("code", CList.Api.Client.OK);
		return ret;
	}

	/**
	 * 更新教材
	 * 
	 * @param book
	 *            教材
	 */
//	@PutMapping(value = "/book/{bid}", produces = JSON_PRODUCES)
	public Map<String, Object> updateBook(@PathVariable String bid, Book book) {
		Map<String, Object> ret = Maps.newHashMap();
		if (Strings.isNullOrEmpty(bid)) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "id is null");
			return ret;
		}
		Book origBook = bookService.findById(bid);
		if (null == origBook) {
			ret.put("code", CList.Api.Client.PROCESSING_FAILED);
			ret.put("desc", "origBook is not exist");
			return ret;
		}
		if (!Strings.isNullOrEmpty(book.getGradeId())) { // 年级Id
			origBook.setGradeId(book.getGradeId());
		}
		if (!Strings.isNullOrEmpty(book.getTitle())) { // 书名
			origBook.setTitle(book.getTitle());
		}
		if (!Strings.isNullOrEmpty(book.getIntro())) { // 书简介
			origBook.setIntro(book.getIntro());
		}
		if (!Strings.isNullOrEmpty(book.getIsbn())) { // isbn 编码
			origBook.setIsbn(book.getIsbn());
		}
		if (!Strings.isNullOrEmpty(book.getImgTfileId())) { // 书封面图片
			origBook.setImgTfileId(book.getImgTfileId());
		}
		if (!Strings.isNullOrEmpty(book.getPublishHouse())) { // 出版社
			origBook.setPublishHouse(book.getPublishHouse());
		}
		bookService.update(origBook);
		ret.put("code", CList.Api.Client.OK);
		ret.put("updatedBook", origBook);
		return ret;
	}

	/**
	 * 查询教材对应的所有版本
	 */
	@GetMapping(value = "/versions", produces = JSON_PRODUCES)
	public Map<String, Object> getBookVersions() {
		Map<String, Object> ret = Maps.newHashMap();
		Map<String, String> vMap = Maps.newHashMap();
		for (VersionDesc v : VersionDesc.values()) {
			vMap.put(v.getDesc(), v.toString());
		}
		ret.put("code", CList.Api.Client.OK);
		ret.put("versions", vMap);
		return ret;
	}

}
