package com.nercl.music.cloud.entity.knowledge;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.resource.Resource;

@Entity
@Table(name = "resource_knowledges")
public class ResourceKnowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8152152798997456571L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	
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
	 * 知识点
	 */
	@Column(name = "knowledge_id")
	private String knowledgeId;

	/**
	 * 知识点
	 */
	@ManyToOne
	@JoinColumn(name = "knowledge_id", insertable = false, updatable = false)
	private Knowledge knowledge;

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

	public String getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public String getId() {
		return id;
	}

}
