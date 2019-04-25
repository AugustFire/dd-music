package com.nercl.music.cloud.entity.classroom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "task_comments")
public class TaskComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2644730587926453684L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 作业评语
	 */
	@Lob
	@Column(columnDefinition = "TEXT")
	private String comment;

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

	private String studentId;

	private String teacherId;

	/**
	 * 作业评星--半星到5星
	 */
	@Enumerated(EnumType.STRING)
	private EunmStar eunmStar;

	private Long createAt;

	public enum EunmStar {

		ZERO, HALF, ONE, ONE_HALF, TWO, TWO_HALF, THREE, THREE_HALF, FOUR, FOUR_HALF, FIVE;

		/**
		 * 判断字符串是否是枚举指定的值,是则返回true
		 */
		public static final Boolean isDefined(String str) {
			EunmStar[] en = EunmStar.values();
			for (int i = 0; i < en.length; i++) {
				if (en[i].toString().equals(str)) {
					return true;
				}
			}
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public EunmStar getEunmStar() {
		return eunmStar;
	}

	public void setEunmStar(EunmStar eunmStar) {
		this.eunmStar = eunmStar;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

}
