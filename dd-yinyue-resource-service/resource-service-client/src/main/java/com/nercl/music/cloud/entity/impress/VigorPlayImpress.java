package com.nercl.music.cloud.entity.impress;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.nercl.music.cloud.entity.resource.Resource;

/**
 * 力度与演奏技法特征
 */
@Entity
@DiscriminatorValue("vigor_play")
public class VigorPlayImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2712795813353711324L;
	
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
