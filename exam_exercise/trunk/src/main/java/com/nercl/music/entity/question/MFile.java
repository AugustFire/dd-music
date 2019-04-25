package com.nercl.music.entity.question;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mfiles")
public class MFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 794116335561076812L;

	@Id
	private String id;

	private String originName;

	private String name;

	private String ext;

	private Integer fileResType;

	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

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

	public Integer getFileResType() {
		return fileResType;
	}

	public void setFileResType(Integer fileResType) {
		this.fileResType = fileResType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
