package com.nercl.music.entity.authorize;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.question.ExamQuestion;

@Entity
@Table(name = "topic_questions")
public class TopicQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1569929475368229179L;

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
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "topic_id", insertable = false, updatable = false)
	private Topic topic;

	/**
	 * 对应题目
	 */
	@Column(name = "exam_question_id")
	private String examQuestionId;

	/**
	 * 对应题目
	 */
	@ManyToOne
	@JoinColumn(name = "exam_question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
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

	public String getId() {
		return id;
	}

}
