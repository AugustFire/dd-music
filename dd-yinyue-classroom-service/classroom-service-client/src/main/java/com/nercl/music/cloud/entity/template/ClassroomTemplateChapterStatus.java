package com.nercl.music.cloud.entity.template;

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

import com.nercl.music.cloud.entity.classroom.Chapter;
import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Entity
@Table(name = "classroom_template_chapter_status")
public class ClassroomTemplateChapterStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7295162240234544813L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应课堂
	 */
	@Column(name = "classroom_id")
	private String classroomId;

	/**
	 * 对应课堂
	 */
	@ManyToOne
	@JoinColumn(name = "classroom_id", insertable = false, updatable = false)
	private ClassRoom classroom;

	/**
	 * 对应模板
	 */
	@Column(name = "template_id")
	private String templateId;

	/**
	 * 对应模板
	 */
	@ManyToOne
	@JoinColumn(name = "template_id", insertable = false, updatable = false)
	private Template template;

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

	@Enumerated(EnumType.STRING)
	private ChapterResourceStatus status;

	/**
	 * 章节资源完成状态
	 */
	public enum ChapterResourceStatus {

		/**
		 * 章节资源状态-已完成
		 */
		COMPLETION("已完成"),

		/**
		 * 章节资源状态-未完成
		 */
		UNCOMPLETION("未完成"),

		/**
		 * 章节资源状态-正在进行
		 */
		DOING("正在进行");

		private String desc;

		private ChapterResourceStatus(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		/**
		 * 判断字符串是否是枚举指定的值,是则返回true
		 */
		public static final Boolean isDefined(String str) {
			ChapterResourceStatus[] en = ChapterResourceStatus.values();
			for (int i = 0; i < en.length; i++) {
				if (en[i].toString().equals(str)) {
					return true;
				}
			}
			return false;
		}

	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public ClassRoom getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassRoom classroom) {
		this.classroom = classroom;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getId() {
		return id;
	}

	public ChapterResourceStatus getStatus() {
		return status;
	}

	public void setStatus(ChapterResourceStatus status) {
		this.status = status;
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

}
