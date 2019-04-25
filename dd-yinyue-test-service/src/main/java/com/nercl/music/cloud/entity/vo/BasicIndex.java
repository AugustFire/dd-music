package com.nercl.music.cloud.entity.vo;

import java.io.Serializable;

/**
 * 基础指标
 */
public class BasicIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8114115753153213574L;

	/**
	 * 班级id
	 */
	private String classId;

	/**
	 * 班级名称
	 */
	private String className;

	/**
	 * 班级人数
	 */
	private Integer classNumber;

	/**
	 * 任课老师id
	 */
	private String teacherId;

	/**
	 * 任课老师姓名
	 */
	private String teacherName;

	/**
	 * 出勤人数
	 */
	private Integer attendanceNo;

	/**
	 * 出勤率
	 */
	private Float attendanceRate;

	/**
	 * 活动次数
	 */
	private Integer activityNo;

	/**
	 * 平均参与人数
	 */
	private Float averageNo;

	/**
	 * 参与度
	 */
	private Float participation;

	/**
	 * 考级人数
	 */
	private Integer gradingTestNo;

	/**
	 * 比赛获奖人数
	 */
	private Integer awardNo;

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

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
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

	public Integer getAttendanceNo() {
		return attendanceNo;
	}

	public void setAttendanceNo(Integer attendanceNo) {
		this.attendanceNo = attendanceNo;
	}

	public Float getAttendanceRate() {
		return attendanceRate;
	}

	public void setAttendanceRate(Float attendanceRate) {
		this.attendanceRate = attendanceRate;
	}

	public Integer getActivityNo() {
		return activityNo;
	}

	public void setActivityNo(Integer activityNo) {
		this.activityNo = activityNo;
	}

	public Float getAverageNo() {
		return averageNo;
	}

	public void setAverageNo(Float averageNo) {
		this.averageNo = averageNo;
	}

	public Float getParticipation() {
		return participation;
	}

	public void setParticipation(Float participation) {
		this.participation = participation;
	}

	public Integer getGradingTestNo() {
		return gradingTestNo;
	}

	public void setGradingTestNo(Integer gradingTestNo) {
		this.gradingTestNo = gradingTestNo;
	}

	public Integer getAwardNo() {
		return awardNo;
	}

	public void setAwardNo(Integer awardNo) {
		this.awardNo = awardNo;
	}

	@Override
	public String toString() {
		return "BasicIndex [classId=" + classId + ", className=" + className + ", classNumber=" + classNumber
				+ ", teacherId=" + teacherId + ", teacherName=" + teacherName + ", attendanceNo=" + attendanceNo
				+ ", attendanceRate=" + attendanceRate + ", activityNo=" + activityNo + ", averageNo=" + averageNo
				+ ", participation=" + participation + ", gradingTestNo=" + gradingTestNo + ", awardNo=" + awardNo
				+ "]";
	}
}
