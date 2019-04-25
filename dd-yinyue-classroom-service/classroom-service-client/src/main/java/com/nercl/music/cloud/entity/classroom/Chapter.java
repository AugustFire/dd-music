package com.nercl.music.cloud.entity.classroom;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "chapters")
public class Chapter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8738351291765491385L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 对应教材
	 */
	@Column(name = "book_id")
	private String bookId;

	/**
	 * 对应教材
	 */
	@ManyToOne
	@JoinColumn(name = "book_id", insertable = false, updatable = false)
	private Book book;

	/**
	 * 父章节
	 */
	@Column(name = "parent_id")
	private String parentId;

	/**
	 * 父章节
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Chapter parent;

	@OneToMany(mappedBy = "parent", cascade = { CascadeType.ALL })
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("orderBy")
	private List<Chapter> children;

	/**
	 * 教学形式
	 */
	@Enumerated(EnumType.STRING)
	private TeachForm teachForm;

	/**
	 * 排序
	 */
	private Integer orderBy;

	@Override
	public boolean equals(Object another) {
		if (null == another) {
			return false;
		}
		if (!(another instanceof Chapter)) {
			return false;
		}
		Chapter chapter = (Chapter) another;
		if (this.getId().equals(chapter.getId())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + getId().hashCode();
		return hash;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Chapter getParent() {
		return parent;
	}

	public void setParent(Chapter parent) {
		this.parent = parent;
	}

	public List<Chapter> getChildren() {
		return children;
	}

	public void setChildren(List<Chapter> children) {
		this.children = children;
	}

	public TeachForm getTeachForm() {
		return teachForm;
	}

	public void setTeachForm(TeachForm teachForm) {
		this.teachForm = teachForm;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
