package com.nercl.music.cloud.entity.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 学段
 * 
 */
@Entity
@Table(name = "learn_stages")
public class LearnStage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4203089438478456670L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 学段名称
	 */
	@Column(unique = true)
	private String title;

	/**
	 * 学段代码
	 */
	@Column(unique = true)
	private String code;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
