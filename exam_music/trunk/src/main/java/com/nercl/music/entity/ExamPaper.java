package com.nercl.music.entity;

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
@Table(name = "exam_papers")
public class ExamPaper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3813879207776721909L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 名字
	 */
	private String title;

	/**
	 * 总分值
	 */
	private Integer score;

	/**
	 * 年度
	 */
	private Integer year;

	/**
	 * 科目类型
	 */
	private Integer subjectType;

	/**
	 * 题数
	 */
	private Integer questionNum;

	/**
	 * 值0-1,0表示最难，1表示最简单
	 */
	private Float difficulty;

	/**
	 * 信度0-1
	 */
	private Float reliability;

	/**
	 * 效度0-1
	 */
	private Float validity;

	/**
	 * 区分度-1-1
	 */
	private Float discrimination;

	/**
	 * 保密等级
	 */
	private Integer secretLevel;

	/**
	 * 是否公开
	 */
	private Boolean isOpen;

	/**
	 * 版本
	 */
	private Long version;

	/**
	 * 答题时间
	 */
	private Integer resolvedTime;
	
	private String unPassReason;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private CheckRecord.Status checkStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Float getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Float difficulty) {
		this.difficulty = difficulty;
	}

	public Float getReliability() {
		return reliability;
	}

	public void setReliability(Float reliability) {
		this.reliability = reliability;
	}

	public Float getValidity() {
		return validity;
	}

	public void setValidity(Float validity) {
		this.validity = validity;
	}

	public Float getDiscrimination() {
		return discrimination;
	}

	public void setDiscrimination(Float discrimination) {
		this.discrimination = discrimination;
	}

	public Integer getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getResolvedTime() {
		return resolvedTime;
	}

	public void setResolvedTime(Integer resolvedTime) {
		this.resolvedTime = resolvedTime;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public CheckRecord.Status getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckRecord.Status checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getUnPassReason() {
		return unPassReason;
	}

	public void setUnPassReason(String unPassReason) {
		this.unPassReason = unPassReason;
	}

}
