package com.nercl.music.cloud.service;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.classroom.VersionDesc;

public interface BookService {

	Book getByGradeAndVersion(String gradeId, VersionDesc version, boolean isFirstVolume);

	Book getByISBN(String isbn);

	boolean save(Book book);

	void update(Book book);

	List<Book> getBooks();

	/**
	 * 根据实体条件查询对应的实体
	 * 
	 * @param book
	 */
	List<Book> findByConditions(Book book) throws Exception;

	/**
	 * 根据id删除教材
	 * 
	 * @param bookId
	 */
	void deleteById(String bookId);

	/**
	 * 根据id查询教材
	 * 
	 * @param bookId
	 */
	Book findById(String bookId);

	List<TeachForm> getTeachForms(VersionDesc versionDesc);

	void init();
}
