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
 * 每个活动对应的班级年级学校信息
 */
@Entity
@Table(name = "activitiy_classes")
public class ActivityClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8833887014292736341L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	@Column(name = "activity_id")
	private String activityId;

	@ManyToOne
	@JoinColumn(name = "activity_id", insertable = false, updatable = false)
	private Activity activity;

	@Column(name = "class_id")
	private String classId;

	@Column(name = "class_name")
	private String className;

	@Column(name = "grade_id")
	private String gradeId;

	@Column(name = "grade_name")
	private String gradeName;

	@Column(name = "school_id")
	private String schoolId;

	@Column(name = "school_name")
	private String schoolName;

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

	public String getId() {
		return id;
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
}