package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@Lob
	@Column(columnDefinition = "TEXT")
	private String content;

	/**
	 * 图片
	 */
	private String optionImage;

	/**
	 * 五线谱
	 */
	private String xmlPath;

	/**
	 * 是否是正确选项
	 */
	private Boolean isTrue;

	/**
	 * 选项对应实际值
	 */
	private String value;

	/**
	 * 对应考题
	 */
	@Column(name = "question_id")
	private String questionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "question_id", insertable = false, updatable = false)
	private Question question;
	
	/**
	 * 对应习题组
	 */
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 对应习题组
	 */
	@ManyToOne
	@JoinColumn(name = "group_id", insertable = false, updatable = false)
	private Group group;

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

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
