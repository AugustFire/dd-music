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
@Table(name = "answer_record_maluations")
public class AnswerRecordMaluation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6081824456487603906L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应答案
	 */
	@Column(name = "answer_record_id")
	private String answerRecordId;

	/**
	 * 对应答案
	 */
	@ManyToOne
	@JoinColumn(name = "answer_record_id", insertable = false, updatable = false)
	private AnswerRecord answerRecord;

	@Enumerated(EnumType.STRING)
	private MaluationOption maluationOption;

	private Integer score;

	public String getAnswerRecordId() {
		return answerRecordId;
	}

	public void setAnswerRecordId(String answerRecordId) {
		this.answerRecordId = answerRecordId;
	}

	public AnswerRecord getAnswerRecord() {
		return answerRecord;
	}

	public void setAnswerRecord(AnswerRecord answerRecord) {
		this.answerRecord = answerRecord;
	}

	public MaluationOption getMaluationOption() {
		return maluationOption;
	}

	public void setMaluationOption(MaluationOption maluationOption) {
		this.maluationOption = maluationOption;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getId() {
		return id;
	}

}
