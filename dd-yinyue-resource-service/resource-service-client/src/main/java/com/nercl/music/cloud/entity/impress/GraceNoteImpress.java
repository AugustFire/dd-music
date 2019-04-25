package com.nercl.music.cloud.entity.impress;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.nercl.music.cloud.entity.resource.Resource;

/**
 * 装饰音与略写记号特征
 */
@Entity
@DiscriminatorValue("grace_note")
public class GraceNoteImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7717550128684037470L;

	/**
	 * 资源
	 */
	@Column(name = "resource_id")
	private String resourceId;

	/**
	 * 资源
	 */
	@ManyToOne
	@JoinColumn(name = "resource_id", insertable = false, updatable = false)
	private Resource resource;

	/**
	 * 摘要
	 */
	private String summary;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
