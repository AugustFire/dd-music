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
@Table(name = "notices")
public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -149386638024334136L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 內容
	 */
	private String content;

	/**
	 * 生成时间
	 */
	private Long creatAt;

	/**
	 * 对应课堂空间
	 */
	@Column(name = "class_room_id")
	private String classRoomId;

	/**
	 * 对应课堂空间
	 */
	@ManyToOne
	@JoinColumn(name = "class_room_id", insertable = false, updatable = false)
	private ClassRoom classRoom;

	/**
	 * 对应课时
	 */
	@Column(name = "chapter_id")
	private String chapterId;

	/**
	 * 对应课时
	 */
	@ManyToOne
	@JoinColumn(name = "chapter_id", insertable = false, updatable = false)
	private Chapter chapter;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Long creatAt) {
		this.creatAt = creatAt;
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

	public String getId() {
		return id;
	}

}
