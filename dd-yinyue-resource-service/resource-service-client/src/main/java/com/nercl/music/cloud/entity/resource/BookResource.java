package com.nercl.music.cloud.entity.resource;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 教本资源
 */
@Entity
@DiscriminatorValue("book")
public class BookResource extends Resource {

	private static final long serialVersionUID = -6253713588953633454L;

	/**
	 * 结构
	 */
	@Enumerated(EnumType.STRING)
	private Structure structure;

	/**
	 * 是否帮助文件
	 */
	private Boolean isHelp;

	@Column(name = "group_no")
	private String groupNo;

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public Boolean getIsHelp() {
		return isHelp;
	}

	public void setIsHelp(Boolean isHelp) {
		this.isHelp = isHelp;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

}
