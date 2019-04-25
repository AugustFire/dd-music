package com.nercl.music.cloud.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.nercl.music.cloud.dao.BookDao;
import com.nercl.music.cloud.entity.classroom.Book;
import com.nercl.music.cloud.entity.classroom.TeachForm;
import com.nercl.music.cloud.entity.classroom.VersionDesc;
import com.nercl.music.util.DataIniter;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Value("${dd-yinyue.classroom.resources}")
	private String resources;

	@Autowired
	private BookDao bookDao;

	@Autowired
	private DataIniter dataIniter;

	@Override
	public Book getByGradeAndVersion(String gradeId, VersionDesc version, boolean isFirstVolume) {
		return bookDao.getByGradeAndVersion(gradeId, version, isFirstVolume);
	}

	@Override
	public Book getByISBN(String isbn) {
		return bookDao.getByISBN(isbn);
	}

	@Override
	public boolean save(Book book) {
		bookDao.save(book);
		return !Strings.isNullOrEmpty(book.getId());
	}

	@Override
	public void update(Book book) {
		bookDao.update(book);
	}

	@Override
	public List<Book> getBooks() {
		return bookDao.getBooks();
	}

	@Override
	public List<Book> findByConditions(Book book) throws Exception {
		return bookDao.findByConditions(book);
	}

	@Override
	public void deleteById(String bookId) {
		bookDao.deleteById(bookId);
	}

	@Override
	public Book findById(String bookId) {
		return bookDao.findByID(bookId);
	}

	@Override
	public List<TeachForm> getTeachForms(VersionDesc versionDesc) {
		return Arrays.stream(TeachForm.values()).filter(teachForm -> teachForm.getVersionDesc().equals(versionDesc))
				.collect(Collectors.toList());
	}

	@Override
	public void init() {
		dataIniter.init();
	}

}