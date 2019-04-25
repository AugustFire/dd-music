package com.nercl.music.cloud.entity.classroom;

/**
 * 课堂作业
 * */
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
@Table(name = "tasks")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1692947187497393472L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String title;

	/**
	 * 对应章节
	 */
	@Column(name = "chapter_id")
	private String chapterId;

	/**
	 * 对应章节
	 */
	@ManyToOne
	@JoinColumn(name = "chapter_id", insertable = false, updatable = false)
	private Chapter chapter;

	/**
	 * 对应课堂
	 */
	@Column(name = "class_room_id")
	private String classRoomId;

	/**
	 * 对应课堂
	 */
	@ManyToOne
	@JoinColumn(name = "class_room_id", insertable = false, updatable = false)
	private ClassRoom classRoom;

	@Enumerated(EnumType.STRING)
	private TaskType taskType;

	/**
	 * 截止时间
	 */
	private Long dendline;

	/**
	 * 生成时间
	 */
	private Long createAt;

	public enum TaskType {

		PRE_CLASSROOM,

		IN_CLASSROOM,

		AFTER_CLASSROOM;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public Long getDendline() {
		return dendline;
	}

	public void setDendline(Long dendline) {
		this.dendline = dendline;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}
}
