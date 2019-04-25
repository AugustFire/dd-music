package com.nercl.music.cloud.entity.classroom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "class_rooms")
public class ClassRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971896958742502317L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 简介
	 */
	private String intro;

	/**
	 * 课程代码
	 */
	private String code;

	/**
	 * 缩略图
	 */
	@Column(name = "img_tfile_id")
	private String imgTfileId;

	/**
	 * 对应老师
	 */
	@Column(name = "teacher_id")
	private String teacherId;

	/**
	 * 对应老师
	 */
	@Column(name = "teacher_name")
	private String teacherName;

	/**
	 * 年级
	 */
	private String gradeId;

	/**
	 * 班级
	 */
	private String classesId;

	/**
	 * 创建时间
	 */
	private Long createAt;

	/**
	 * 对应教材
	 */
	private String bookId;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getId() {
		return id;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getImgTfileId() {
		return imgTfileId;
	}

	public void setImgTfileId(String imgTfileId) {
		this.imgTfileId = imgTfileId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getClassesId() {
		return classesId;
	}

	public void setClassesId(String classesId) {
		this.classesId = classesId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

}
