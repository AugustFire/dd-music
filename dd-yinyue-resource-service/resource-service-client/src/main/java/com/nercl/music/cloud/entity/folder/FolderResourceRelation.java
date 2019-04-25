package com.nercl.music.cloud.entity.folder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.resource.Resource;

@Entity
@Table(name = "folder_resource_relations")
public class FolderResourceRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6130786534911808103L;

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
	@OneToOne
	@JoinColumn(name = "resource_id", insertable = false, updatable = false)
	private Resource resource;

	/**
	 * folder
	 */
	@Column(name = "folder_id")
	private String folderId;

	@ManyToOne
	@JoinColumn(name = "folder_id", insertable = false, updatable = false)
	private Folder folder;

	/**
	 * 区分是否是收藏的资源
	 */
	private Boolean isCollection;

	/**
	 * 是否删除，进入回收站
	 */
	private Boolean isDeleted;

	/**
	 * 是否还原，从回收站还原
	 */
	private Boolean isRestored;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Long createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Long updateTime;

	public String getId() {
		return id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsRestored() {
		return isRestored;
	}

	public void setIsRestored(Boolean isRestored) {
		this.isRestored = isRestored;
	}

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

}
