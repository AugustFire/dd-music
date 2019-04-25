package com.nercl.music.entity.authorize;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "topic_subjects")
public class TopicSubject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917938753670756769L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应专题
	 */
	@Column(name = "topic_id")
	private String topicId;

	/**
	 * 对应专题
	 */
	@Column(name = "topic_title")
	private String topicTitle;

	/**
	 * 对应主题
	 */
	@Column(name = "subject_id")
	private String subjectId;

	/**
	 * 对应主题
	 */
	@Column(name = "subject_title")
	private String subjectTitle;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectTitle() {
		return subjectTitle;
	}

	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}

	public String getId() {
		return id;
	}

}
