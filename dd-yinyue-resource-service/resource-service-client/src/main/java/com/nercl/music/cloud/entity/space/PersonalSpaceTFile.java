package com.nercl.music.cloud.entity.space;

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
@Table(name = "personal_space_tfiles")
public class PersonalSpaceTFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8499523219314593062L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应空间
	 */
	@Column(name = "personal_space_id")
	private String personalSpaceId;

	/**
	 * 对应空间
	 */
	@ManyToOne
	@JoinColumn(name = "personal_space_id", insertable = false, updatable = false)
	private PersonalSpace personalSpace;

	/**
	 * 对应资源
	 */
	@Column(name = "tfile_id")
	private String tfileId;

	/**
	 * 对应资源
	 */
	@ManyToOne
	@JoinColumn(name = "tfile_id", insertable = false, updatable = false)
	private Resource resource;

	public String getPersonalSpaceId() {
		return personalSpaceId;
	}

	public void setPersonalSpaceId(String personalSpaceId) {
		this.personalSpaceId = personalSpaceId;
	}

	public PersonalSpace getPersonalSpace() {
		return personalSpace;
	}

	public void setPersonalSpace(PersonalSpace personalSpace) {
		this.personalSpace = personalSpace;
	}

	public String getTfileId() {
		return tfileId;
	}

	public void setTfileId(String tfileId) {
		this.tfileId = tfileId;
	}


	public String getId() {
		return id;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
