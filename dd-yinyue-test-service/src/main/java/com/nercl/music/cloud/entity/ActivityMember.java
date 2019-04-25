package com.nercl.music.cloud.entity;

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
 * 活动中的成员
 */
@Entity
@Table(name = "activity_members")
public class ActivityMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335474446924997958L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 活动id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 活动
	 */
	@ManyToOne
	@JoinColumn(name = "activity_id", insertable = false, updatable = false)
	private Activity activity;

	/**
	 * 学生id
	 */
	@Column(name = "student_id")
	private String studentId;

	/**
	 * 学生姓名
	 */
	@Column(name = "student_name")
	private String studentName;

	/**
	 * 班级id
	 */
	@Column(name = "class_id")
	private String classId;

	/**
	 * 班级名称
	 */
	@Column(name = "class_name")
	private String className;

	/**
	 * 年级id
	 */
	@Column(name = "grade_id")
	private String gradeId;

	/**
	 * 年级名称
	 */
	@Column(name = "grade_name")
	private String gradeName;

	/**
	 * 学校id
	 */
	@Column(name = "school_id")
	private String schoolId;

	/**
	 * 学校名称
	 */
	@Column(name = "school_name")
	private String schoolName;

	/**
	 * 是否参与活动
	 */
	@Column(name = "joined")
	private Boolean joined;

	public String getId() {
		return id;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Boolean getJoined() {
		return joined;
	}

	public void setJoined(Boolean joined) {
		this.joined = joined;
	}
}
