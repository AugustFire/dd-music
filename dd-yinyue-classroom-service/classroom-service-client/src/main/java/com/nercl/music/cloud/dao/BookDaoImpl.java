package com.nercl.music.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.VersionDesc;

@Repository
public class BookDaoImpl extends AbstractBaseDaoImpl<Book, String> implements BookDao {

	@Override
	public Book getByISBN(String isbn) {
		String jpql = "from Book b where b.isbn = ?1";
		List<Book> books = this.executeQueryWithoutPaging(jpql, isbn);
		return null != books && !books.isEmpty() ? books.get(0) : null;
	}

	@Override
	public Book getOneBook() {
		String jpql = "from Book b";
		List<Book> books = this.executeQueryWithoutPaging(jpql);
		return null != books && !books.isEmpty() ? books.get(0) : null;
	}

	@Override
	public List<Book> getBooks() {
		String jpql = "from Book b";
		List<Book> books = this.executeQueryWithoutPaging(jpql);
		return books;
	}

	@Override
	public Book getByGradeAndVersion(String gradeId, VersionDesc version, boolean isFirstVolume) {
		String jpql = "from Book b where b.gradeId = ?1 and b.version = ?2 and b.isFirstVolume = ?3";
		List<Book> books = this.executeQueryWithoutPaging(jpql, gradeId, version, isFirstVolume);
		return null != books && !books.isEmpty() ? books.get(0) : null;
	}

}
