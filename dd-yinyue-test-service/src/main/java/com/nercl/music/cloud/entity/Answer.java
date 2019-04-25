package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "answers")
public class Answer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2117882110926151203L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 对应资源文件路径
	 */
	private String resource;

	/**
	 * 对应考题
	 */
	@Column(name = "question_id")
	private String questionId;

	/**
	 * 对应考题
	 */
	@OneToOne
	@JoinColumn(name = "question_id", insertable = false, updatable = false)
	private Question question;

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
