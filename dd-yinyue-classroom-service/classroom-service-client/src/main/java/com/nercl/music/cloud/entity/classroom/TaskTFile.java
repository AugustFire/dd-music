package com.nercl.music.cloud.entity.classroom;

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
@Table(name = "task_tfiles")
public class TaskTFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7854686352183923308L;

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
	 * 对应资源
	 */
	@Column(name = "tfile_id")
	private String tfileId;

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

	public String getTfileId() {
		return tfileId;
	}

	public void setTfileId(String tfileId) {
		this.tfileId = tfileId;
	}

	public String getId() {
		return id;
	}

}
