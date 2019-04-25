package com.nercl.music.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "exams")
public class Exam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -605183832216221834L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 年度
	 */
	private Integer year;

	/**
	 * 月份
	 */
	private Integer month;

	/**
	 * 考试名称
	 */
	private String title;

	/**
	 * 简介
	 */
	private String intro;

	/**
	 * 开始时间
	 */
	private Long startAt;

	/**
	 * 结束时间
	 */
	private Long endAt;

	/**
	 * 考试状态
	 */
	@Enumerated(EnumType.STRING)
	private ExamStatus examStatus;

	/**
	 * 考点
	 */
	@ManyToMany(targetEntity = ExamPoint.class, cascade = { CascadeType.ALL })
	@JoinTable(name = "exams_exam_points", joinColumns = { @JoinColumn(name = "exam_id") }, inverseJoinColumns = {
	        @JoinColumn(name = "exam_point_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExamPoint> examPoints;

	private Boolean usedToExam;

	public enum ExamStatus {
	    /**
	     * 有效
	     */
		VALID("有效"),

		/**
		 * 过期
		 */
		EXPIRED("过期");

	private String desc;

	private ExamStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
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

	public void setId(String id) {
		this.id = id;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Long getStartAt() {
		return startAt;
	}

	public void setStartAt(Long startAt) {
		this.startAt = startAt;
	}

	public Long getEndAt() {
		return endAt;
	}

	public void setEndAt(Long endAt) {
		this.endAt = endAt;
	}

	public ExamStatus getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(ExamStatus examStatus) {
		this.examStatus = examStatus;
	}

	public List<ExamPoint> getExamPoints() {
		return examPoints;
	}

	public void setExamPoints(List<ExamPoint> examPoints) {
		this.examPoints = examPoints;
	}

	public Boolean getUsedToExam() {
		return usedToExam;
	}

	public void setUsedToExam(Boolean usedToExam) {
		this.usedToExam = usedToExam;
	}

}
