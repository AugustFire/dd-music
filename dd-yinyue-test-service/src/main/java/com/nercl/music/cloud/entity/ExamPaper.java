package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	 * 创建时间
	 */
	@Column(name = "create_at")
	private Long createAt;

	/**
	 * 修改时间
	 */
	@Column(name = "update_at")
	private Long updateAt;

	/**
	 * 制作人
	 */
	private String producerId;

	/**
	 * 制作人
	 */
	private String producerName;

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
	 * 答题时间(分钟)
	 */
	private Integer resolvedTime;

	@Enumerated(EnumType.STRING)
	private ExamPaperType examPaperType;

	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * 对应考试
	 */
	@Column(name = "exam_id")
	private String examId;

	/**
	 * 对应考试
	 */
	@ManyToOne
	@JoinColumn(name = "exam_id", insertable = false, updatable = false)
	private Exam exam;

	public enum Status {

		FINISHED,

		NON_FINISHED;

	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public ExamPaperType getExamPaperType() {
		return examPaperType;
	}

	public void setExamPaperType(ExamPaperType examPaperType) {
		this.examPaperType = examPaperType;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Long updateAt) {
		this.updateAt = updateAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

}
