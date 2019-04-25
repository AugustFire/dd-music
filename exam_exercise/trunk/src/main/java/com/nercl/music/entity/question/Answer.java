package com.nercl.music.entity.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	@Transient
	private String content2;

	/**
	 * 对应资源文件路径 xml文件路径
	 */
	private String xmlPath;

	@Transient
	private String xmlPath2;

	/**
	 * 对应考题
	 */
	@Column(name = "exam_question_id")
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@OneToOne
	@JoinColumn(name = "exam_question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getXmlPath2() {
		return xmlPath2;
	}

	public void setXmlPath2(String xmlPath2) {
		this.xmlPath2 = xmlPath2;
	}

}
