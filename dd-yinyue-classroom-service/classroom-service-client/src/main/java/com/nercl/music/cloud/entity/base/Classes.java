package com.nercl.music.cloud.entity.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 班级
 * 
 */
@Entity
@Table(name = "classes")
public class Classes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2592777711031338265L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 开始年度
	 */
	@Column(name = "start_year")
	private Integer startYear;

	/**
	 * 结束年度
	 */
	@Column(name = "end_year")
	private Integer endYear;

	/**
	 * 班级名称
	 */
	private String name;

	/**
	 * 教师
	 */
	@Column(name = "teacher_id")
	private String teacherId;

	/**
	 * 教师
	 */
	@Column(name = "teacher_name")
	private String teacherName;

	/**
	 * 年级
	 */
	@Column(name = "grade_id")
	private String gradeId;

	/**
	 * 年级
	 */
	@ManyToOne
	@JoinColumn(name = "grade_id", insertable = false, updatable = false)
	private Grade grade;

	/**
	 * 学校
	 */
	@Column(name = "school_id")
	private String schoolId;

	@ManyToOne
	@JoinColumn(name = "school_id", insertable = false, updatable = false)
	private School school;

	/**
	 * 是否培训班
	 */
	private Boolean trained;
	
	/**
	 * 排序
	 */
	@Column(name = "order_by")
	private String orderBy;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	public Boolean getTrained() {
		return trained;
	}

	public void setTrained(Boolean trained) {
		this.trained = trained;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
