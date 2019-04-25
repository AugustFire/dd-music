package com.nercl.music.entity.user;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "exercisers")
public class Exerciser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7715386819500457798L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 身份证
	 */
	@Column(nullable = false)
	private String idcard;

	/**
	 * 学校
	 */
	@Column
	private String school;

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

	private String token;

	private Long createAt;

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

	public String getId() {
		return id;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
