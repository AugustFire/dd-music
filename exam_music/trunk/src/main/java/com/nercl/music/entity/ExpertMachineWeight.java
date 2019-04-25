package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "expert_machine_weights")
public class ExpertMachineWeight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -984861633654508491L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

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

	/**
	 * 对应考卷
	 */
	@Column(name = "exam_paper_id")
	private String examPaperId;

	/**
	 * 对应考卷
	 */
	@ManyToOne
	@JoinColumn(name = "exam_paper_id", insertable = false, updatable = false)
	private ExamPaper examPaper;

	/**
	 * 专家权重
	 */
	private Integer expertWeight;

	/**
	 * 机器权重
	 */
	private Integer machineWeight;

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

	public String getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(String examPaperId) {
		this.examPaperId = examPaperId;
	}

	public ExamPaper getExamPaper() {
		return examPaper;
	}

	public void setExamPaper(ExamPaper examPaper) {
		this.examPaper = examPaper;
	}

	public Integer getExpertWeight() {
		return expertWeight;
	}

	public void setExpertWeight(Integer expertWeight) {
		this.expertWeight = expertWeight;
	}

	public Integer getMachineWeight() {
		return machineWeight;
	}

	public void setMachineWeight(Integer machineWeight) {
		this.machineWeight = machineWeight;
	}

	public String getId() {
		return id;
	}

}
