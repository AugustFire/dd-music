package com.nercl.music.cloud.entity.classroom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.base.Grade;

@Entity
@Table(name = "books")
public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2072214628540077825L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String title;

	private String intro;

	private String isbn;

	private String publishHouse;

	private String imgTfileId;

	/**
	 * 上册or下册
	 */
	@Column(name = "is_first_volume")
	private Boolean isFirstVolume;

	@Enumerated(EnumType.STRING)
	private VersionDesc version;

	/**
	 * 年级Id
	 */
	@Column(name = "grade_id")
	private String gradeId;

	@ManyToOne
	@JoinColumn(name = "grade_id", insertable = false, updatable = false)
	private Grade Grade;

	public Grade getGrade() {
		return Grade;
	}

	public void setGrade(Grade Grade) {
		this.Grade = Grade;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPublishHouse() {
		return publishHouse;
	}

	public void setPublishHouse(String publishHouse) {
		this.publishHouse = publishHouse;
	}

	public String getId() {
		return id;
	}

	public String getImgTfileId() {
		return imgTfileId;
	}

	public void setImgTfileId(String imgTfileId) {
		this.imgTfileId = imgTfileId;
	}

	public VersionDesc getVersion() {
		return version;
	}

	public void setVersion(VersionDesc version) {
		this.version = version;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Boolean getIsFirstVolume() {
		return isFirstVolume;
	}

	public void setIsFirstVolume(Boolean isFirstVolume) {
		this.isFirstVolume = isFirstVolume;
	}

}
