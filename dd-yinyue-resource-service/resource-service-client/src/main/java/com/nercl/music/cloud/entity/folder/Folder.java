package com.nercl.music.cloud.entity.folder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "folders")
public class Folder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5377452455112527490L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 父级
	 */
	@Column(name = "parent_id")
	private String parentId;

	/**
	 * 父级
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Folder parent;

	/**
	 * 文件夹名称
	 */
	@Column(name = "folder_name")
	private String folderName;

	/**
	 * 创建者
	 */
	@Column(name = "user_id")
	private String userId;

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

	public String getId() {
		return id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Boolean getIsCollection() {
		return isCollection;
	}

	public void setIsCollection(Boolean isCollection) {
		this.isCollection = isCollection;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Folder getParent() {
		return parent;
	}

	public void setParent(Folder parent) {
		this.parent = parent;
	}

	public Boolean getIsRestored() {
		return isRestored;
	}

	public void setIsRestored(Boolean isRestored) {
		this.isRestored = isRestored;
	}

}
