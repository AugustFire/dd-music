package com.nercl.music.cloud.dao;

import java.util.List;

import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.VersionDesc;

public interface BookDao extends BaseDao<Book, String> {

	Book getByISBN(String isbn);

	Book getOneBook();

	List<Book> getBooks();

	Book getByGradeAndVersion(String gradeId, VersionDesc version, boolean isFirstVolume);

}
