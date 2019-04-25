package com.nercl.music.entity.user;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "experts")
public class Expert implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3284402354669046154L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 职称
	 */
	private String jobTitle;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 简介
	 */
	private String intro;

	/**
	 * person
	 */
	@Column(name = "person_id", nullable = false)
	private String personId;

	/**
	 * person
	 */
	@OneToOne(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;

	private Long createAt;

	/**
	 * 分组
	 */
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 分组
	 */
	@ManyToOne
	@JoinColumn(name = "group_id", insertable = false, updatable = false)
	private AbstractGroup group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public AbstractGroup getGroup() {
		return group;
	}

	public void setGroup(AbstractGroup group) {
		this.group = group;
	}

}
