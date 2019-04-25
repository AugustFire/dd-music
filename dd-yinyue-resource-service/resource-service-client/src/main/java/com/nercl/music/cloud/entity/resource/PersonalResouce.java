package com.nercl.music.cloud.entity.resource;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("personal")
public class PersonalResouce extends Resource {

	/**
	 * 个人资源
	 */
	private static final long serialVersionUID = -2043313720281236913L;

	/**
	 * 对应个人
	 */
	@Column(name = "person_id")
	private String personId;

	/**
	 * 是否公开
	 */
	@Column(name = "is_public")
	private Boolean isPublic;

	/**
	 * 对应空间
	 */
	@Column(name = "space_id")
	private String spaceId;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}

}
