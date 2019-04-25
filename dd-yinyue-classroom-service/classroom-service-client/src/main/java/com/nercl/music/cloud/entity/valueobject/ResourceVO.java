package com.nercl.music.cloud.entity.valueobject;

import java.io.Serializable;

public class ResourceVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6676293077168551383L;

	private String id;

	/**
	 * 资源名称
	 */
	private String name;

	private String ext;

	/**
	 * 大小（KB）
	 */
	private Long size;

	/**
	 * 上传时间
	 */
	private Long createAt;

	/**
	 * 云端id
	 */
	private String cloudId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getId() {
		return id;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
}
