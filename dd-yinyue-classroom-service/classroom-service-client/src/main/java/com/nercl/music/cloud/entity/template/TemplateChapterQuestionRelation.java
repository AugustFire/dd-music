package com.nercl.music.cloud.entity.template;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Entity
@Table(name = "template_chapter_question_relations")
public class TemplateChapterQuestionRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 54295033835911802L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应章节
	 */
	@ManyToOne
	@JoinColumn(name = "chapter_id", insertable = false, updatable = false)
	private Chapter chapter;

	/**
	 * 对应章节
	 */
	@Column(name = "chapter_id")
	private String chapterId;

	/**
	 * 对应模板
	 */
	@ManyToOne
	@JoinColumn(name = "template_id", insertable = false, updatable = false)
	private Template template;

	/**
	 * 对应模板
	 */
	@Column(name = "template_id")
	private String templateId;

	/**
	 * 对应课堂
	 */
	@ManyToOne
	@JoinColumn(name = "class_room_id", insertable = false, updatable = false)
	private ClassRoom classRoom;

	/**
	 * 对应课堂
	 */
	@Column(name = "class_room_id")
	private String classRoomId;

	/**
	 * 试题Id
	 */
	private String questionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
