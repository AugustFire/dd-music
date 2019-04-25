package com.nercl.music.cloud.entity.template;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.classroom.Chapter;

@Entity
@Table(name = "temlate_chapter_resources", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "chapter_id", "resource_id", "song_id", "template_id" }) })
public class TemplateChapterResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1206840691818370083L;

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
	 * 对应资源
	 */
	@Column(name = "resource_id")
	private String resourceId;

	/**
	 * 对应歌曲
	 */
	@Column(name = "song_id")
	private String songId;

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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getId() {
		return id;
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

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}
}
