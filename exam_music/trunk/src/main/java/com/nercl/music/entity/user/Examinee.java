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
@Table(name = "examinees")
public class Examinee implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2211929990293485299L;

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
	 * 准考证号
	 */
	@Column(nullable = false)
	private String examNo;

	/**
	 * 照片
	 */
	@Column(nullable = false)
	private String photo;

	/**
	 * 学校
	 */
	@Column(nullable = false)
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

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getExamNo() {
		return examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
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
