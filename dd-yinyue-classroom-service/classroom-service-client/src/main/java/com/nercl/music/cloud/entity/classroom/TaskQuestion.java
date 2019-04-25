package com.nercl.music.cloud.entity.classroom;

/**
 * 课堂作业
 * */
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
@Table(name = "task_questions")
public class TaskQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8229359478362067320L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应作业
	 */
	@Column(name = "task_id")
	private String taskId;

	/**
	 * 对应作业
	 */
	@ManyToOne
	@JoinColumn(name = "task_id", insertable = false, updatable = false)
	private Task task;

	/**
	 * 题目Id
	 */
	@Column(name = "question_id")
	private String questionId;

	/**
	 * 分数
	 */
	private Float score;

	public String getId() {
		return id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
