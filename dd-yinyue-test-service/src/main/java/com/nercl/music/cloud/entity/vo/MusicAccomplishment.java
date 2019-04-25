package com.nercl.music.cloud.entity.vo;

import java.io.Serializable;

/**
 * 音乐素养
 */
public class MusicAccomplishment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 107651811425528463L;

	/**
	 * 班级id
	 */
	private String classId;

	/**
	 * 班级名称
	 */
	private String className;

	/**
	 * 班级总人数
	 */
	private Long totalAmount;

	/**
	 * 任课老师id
	 */
	private String teacherId;

	/**
	 * 任课老师名称
	 */
	private String teacherName;

	/**
	 * 班级平均分
	 */
	private Float average;

	/**
	 * 及格率
	 */
	private Float passRate;

	/**
	 * 优秀率
	 */
	private Float excellentRate;

	/**
	 * 分数区间在[0,60)的人数
	 */
	private Integer spread60;

	/**
	 * 分数区间在[60,70) 的人数
	 */
	private Integer spread60_70;

	/**
	 * 分数区间在[70,80) 的人数
	 */
	private Integer spread70_80;

	/**
	 * 分数区间在[80,90) 的人数
	 */
	private Integer spread80_90;

	/**
	 * 分数区间在[90,...) 的人数
	 */
	private Integer spread90;

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

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
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

	public Float getAverage() {
		return average;
	}

	public void setAverage(Float average) {
		this.average = average;
	}

	public Float getPassRate() {
		return passRate;
	}

	public void setPassRate(Float passRate) {
		this.passRate = passRate;
	}

	public Float getExcellentRate() {
		return excellentRate;
	}

	public void setExcellentRate(Float excellentRate) {
		this.excellentRate = excellentRate;
	}

	public Integer getSpread60() {
		return spread60;
	}

	public void setSpread60(Integer spread60) {
		this.spread60 = spread60;
	}

	public Integer getSpread60_70() {
		return spread60_70;
	}

	public void setSpread60_70(Integer spread60_70) {
		this.spread60_70 = spread60_70;
	}

	public Integer getSpread70_80() {
		return spread70_80;
	}

	public void setSpread70_80(Integer spread70_80) {
		this.spread70_80 = spread70_80;
	}

	public Integer getSpread80_90() {
		return spread80_90;
	}

	public void setSpread80_90(Integer spread80_90) {
		this.spread80_90 = spread80_90;
	}

	public Integer getSpread90() {
		return spread90;
	}

	public void setSpread90(Integer spread90) {
		this.spread90 = spread90;
	}

	@Override
	public String toString() {
		return "MusicAccomplishment [classId=" + classId + ", className=" + className + ", totalAmount=" + totalAmount
				+ ", teacherName=" + teacherName + ", average=" + average + ", passRate=" + passRate
				+ ", excellentRate=" + excellentRate + ", spread60=" + spread60 + ", spread60_70=" + spread60_70
				+ ", spread70_80=" + spread70_80 + ", spread80_90=" + spread80_90 + ", spread90=" + spread90 + "]";
	}

}
