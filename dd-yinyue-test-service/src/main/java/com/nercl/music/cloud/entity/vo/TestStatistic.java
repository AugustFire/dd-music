package com.nercl.music.cloud.entity.vo;

import java.io.Serializable;

public class TestStatistic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8412965024259372432L;

	/**
	 * 班级id
	 */
	private String classId;

	/**
	 * 班级名称
	 */
	private String className;

	/**
	 * 任课老师id
	 */
	private String teacherId;

	/**
	 * 任课老师名称
	 */
	private String teacherName;

	/**
	 * 知识考试-平均分
	 */
	private Float knowledgeAverageScore;

	/**
	 * 知识考试-及格率
	 */
	private Float knowledgePassRate;

	/**
	 * 知识考试-优秀率
	 */
	private Float knowledgeExcellentRate;

	/**
	 * 表演考试-平均分
	 */
	private Float actAverageScore;

	/**
	 * 表演考试-及格率
	 */
	private Float actPassRate;

	/**
	 * 表演考试-优秀率
	 */
	private Float actExcellentRate;

	/**
	 * 总分-平均分
	 */
	private Float totalAverageScore;

	/**
	 * 总分-及格率
	 */
	private Float totalPassRate;

	/**
	 * 总分-优秀率
	 */
	private Float totalExcellentRate;

	/**
	 * 总分排名
	 */
	private Integer rank;

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

	public Float getKnowledgeAverageScore() {
		return knowledgeAverageScore;
	}

	public void setKnowledgeAverageScore(Float knowledgeAverageScore) {
		this.knowledgeAverageScore = knowledgeAverageScore;
	}

	public Float getKnowledgePassRate() {
		return knowledgePassRate;
	}

	public void setKnowledgePassRate(Float knowledgePassRate) {
		this.knowledgePassRate = knowledgePassRate;
	}

	public Float getKnowledgeExcellentRate() {
		return knowledgeExcellentRate;
	}

	public void setKnowledgeExcellentRate(Float knowledgeExcellentRate) {
		this.knowledgeExcellentRate = knowledgeExcellentRate;
	}

	public Float getActAverageScore() {
		return actAverageScore;
	}

	public void setActAverageScore(Float actAverageScore) {
		this.actAverageScore = actAverageScore;
	}

	public Float getActPassRate() {
		return actPassRate;
	}

	public void setActPassRate(Float actPassRate) {
		this.actPassRate = actPassRate;
	}

	public Float getActExcellentRate() {
		return actExcellentRate;
	}

	public void setActExcellentRate(Float actExcellentRate) {
		this.actExcellentRate = actExcellentRate;
	}

	public Float getTotalAverageScore() {
		return totalAverageScore;
	}

	public void setTotalAverageScore(Float totalAverageScore) {
		this.totalAverageScore = totalAverageScore;
	}

	public Float getTotalPassRate() {
		return totalPassRate;
	}

	public void setTotalPassRate(Float totalPassRate) {
		this.totalPassRate = totalPassRate;
	}

	public Float getTotalExcellentRate() {
		return totalExcellentRate;
	}

	public void setTotalExcellentRate(Float totalExcellentRate) {
		this.totalExcellentRate = totalExcellentRate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "TestStatistic [classId=" + classId + ", className=" + className + ", teacherId=" + teacherId
				+ ", teacherName=" + teacherName + ", knowledgeAverageScore=" + knowledgeAverageScore
				+ ", knowledgePassRate=" + knowledgePassRate + ", knowledgeExcellentRate=" + knowledgeExcellentRate
				+ ", actAverageScore=" + actAverageScore + ", actPassRate=" + actPassRate + ", actExcellentRate="
				+ actExcellentRate + ", totalAverageScore=" + totalAverageScore + ", totalPassRate=" + totalPassRate
				+ ", totalExcellentRate=" + totalExcellentRate + "]";
	}
}
