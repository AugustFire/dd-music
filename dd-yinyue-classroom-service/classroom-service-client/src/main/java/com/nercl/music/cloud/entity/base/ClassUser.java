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
 * 班级学生
 * 
 */
@Entity
@Table(name = "class_users")
public class ClassUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4019476675822899616L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 班级
	 */
	@Column(name = "class_id")
	private String classId;

	/**
	 * 班级
	 */
	@ManyToOne
	@JoinColumn(name = "class_id", insertable = false, updatable = false)
	private Classes classes;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "is_teacher")
	private Boolean isTeacher;

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

	/**
	 * 学校
	 */
	@ManyToOne
	@JoinColumn(name = "school_id", insertable = false, updatable = false)
	private School school;

	@Override
	public boolean equals(Object another) {
		if (null == another) {
			return false;
		}
		if (!(another instanceof ClassUser)) {
			return false;
		}
		ClassUser cu = (ClassUser) another;
		return this.getId().equals(cu.getId());
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + getId().hashCode();
		return hash;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Classes getClasses() {
		return classes;
	}

	public void setClasses(Classes classes) {
		this.classes = classes;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(Boolean isTeacher) {
		this.isTeacher = isTeacher;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
