package com.nercl.music.cloud.entity.classroom;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "chapter_classrooms", indexes = { @Index(columnList = "deleted", unique = false) })
public class ChapterClassRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 440779752497896675L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

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

	private Boolean deleted;

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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getId() {
		return id;
	}

}
