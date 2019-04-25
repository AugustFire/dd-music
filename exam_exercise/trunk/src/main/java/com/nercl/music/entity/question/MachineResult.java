package com.nercl.music.entity.question;

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
@Table(name = "machine_results")
public class MachineResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7675867677165350921L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应考题
	 */
	@Column(name = "question_id")
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	/**
	 * 对应答题记录
	 */
	@Column(name = "answer_record_id")
	private String answerRecordId;

	/**
	 * 对应答题记录
	 */
	@ManyToOne
	@JoinColumn(name = "answer_record_id", insertable = false, updatable = false)
	private ExerciserAnswerRecord answerRecord;

	/**
	 * 分值
	 */
	private Integer score;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public String getExamQuestionId() {
		return examQuestionId;
	}

	public void setExamQuestionId(String examQuestionId) {
		this.examQuestionId = examQuestionId;
	}

	public ExamQuestion getExamQuestion() {
		return examQuestion;
	}

	public void setExamQuestion(ExamQuestion examQuestion) {
		this.examQuestion = examQuestion;
	}

	public String getAnswerRecordId() {
		return answerRecordId;
	}

	public void setAnswerRecordId(String answerRecordId) {
		this.answerRecordId = answerRecordId;
	}

	public ExerciserAnswerRecord getAnswerRecord() {
		return answerRecord;
	}

	public void setAnswerRecord(ExerciserAnswerRecord answerRecord) {
		this.answerRecord = answerRecord;
	}

}
