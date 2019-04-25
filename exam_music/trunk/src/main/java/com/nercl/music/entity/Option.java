package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "options")
public class Option implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6780298638648642909L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 内容
	 */
	@Transient
	private String content2;

	/**
	 * 图片
	 */
	private String optionImage;

	/**
	 * 五线谱
	 */
	private String xmlPath;

	/**
	 * 五线谱
	 */
	@Transient
	private String xmlPath2;

	/**
	 * 是否是正确选项
	 */
	private boolean isTrue;

	/**
	 * 选项对应实际值
	 */
	private String value;

	/**
	 * 对应考题
	 */
	@Column(name = "exam_question_id")
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
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

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOptionImage() {
		return optionImage;
	}

	public void setOptionImage(String optionImage) {
		this.optionImage = optionImage;
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
