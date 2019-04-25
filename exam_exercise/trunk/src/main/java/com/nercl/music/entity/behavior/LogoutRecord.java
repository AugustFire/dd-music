package com.nercl.music.entity.behavior;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.user.Person;

@Entity
@Table(name = "logout_records")
public class LogoutRecord implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5390645810853501754L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 生成时间
	 */
	private Long createAt;

	/**
	 * 持续时间（单位为秒）
	 */
	private Integer duration;

	/**
	 * 持续时间（时分秒）
	 */
	@Transient
	private String durationStr;

	/**
	 * 对应练习者
	 */
	@Column(name = "person_id")
	private String personId;

	/**
	 * 对应练习者
	 */
	@ManyToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
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

	public String getId() {
		return id;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDurationStr() {
		return durationStr;
	}

	public void setDurationStr(String durationStr) {
		this.durationStr = durationStr;
	}
}
