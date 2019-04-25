package com.nercl.music.cloud.entity.space;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "personal_spaces")
public class PersonalSpace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078448437342341209L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String userId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 容量
	 */
	private Long size;

	/**
	 * 已使用
	 */
	private Long usedSize;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(Long usedSize) {
		this.usedSize = usedSize;
	}

	public String getId() {
		return id;
	}

}
