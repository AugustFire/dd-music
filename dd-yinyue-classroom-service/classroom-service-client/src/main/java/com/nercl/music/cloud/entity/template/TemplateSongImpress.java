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

@Entity
@Table(name = "song_impress_temlates", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "song_id", "impress_id", "template_id" }) })
public class TemplateSongImpress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1415406455158774145L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应歌曲
	 */
	@Column(name = "song_id")
	private String songId;

	/**
	 * 对应特征
	 */
	@Column(name = "impress_id")
	private String impressId;

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

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}

	public String getImpressId() {
		return impressId;
	}

	public void setImpressId(String impressId) {
		this.impressId = impressId;
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

}
