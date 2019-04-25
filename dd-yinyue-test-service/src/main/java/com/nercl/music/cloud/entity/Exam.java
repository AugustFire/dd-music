package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "exams")
public class Exam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6426117956691223867L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String grade;

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
	 * 制作人
	 */
	private String producerId;

	/**
	 * 制作人
	 */
	private String producerName;

	/**
	 * 学校id
	 */
	private String schoolId;

	/**
	 * 考试状态
	 */
	@Enumerated(EnumType.STRING)
	private ExamStatus examStatus;

	@Enumerated(EnumType.STRING)
	private ExamType examType;

	/**
	 * 对应章节
	 */
	@Column(name = "chapter_id")
	private String chapterId;

	private Long startAt;

	private Long endAt;

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

		/**
		 * 判断字符串是否是枚举指定的值,是则返回true
		 */
		public static final Boolean isDefined(String str) {
			ExamStatus[] en = ExamStatus.values();
			for (int i = 0; i < en.length; i++) {
				if (en[i].toString().equals(str)) {
					return true;
				}
			}
			return false;
		}

	}

	public enum ExamType {

		/**
		 * 单元测试
		 */
		CHAPTER_TEST,

		/**
		 * 期中测试
		 */
		MIDDLE_TERM_TEST,

		/**
		 * 期末考试
		 */
		FINAL_TERM_TEST,

		/**
		 * 其他
		 */
		OTHER;

		/**
		 * 判断字符串是否是枚举指定的值,是则返回true
		 */
		public static final Boolean IsDefined(String str) {
			ExamType[] en = ExamType.values();
			for (int i = 0; i < en.length; i++) {
				if (en[i].toString().equals(str)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 考试创建时间
	 */
	@Column(name = "create_at")
	private Long createAt;

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

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public ExamStatus getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(ExamStatus examStatus) {
		this.examStatus = examStatus;
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

}
