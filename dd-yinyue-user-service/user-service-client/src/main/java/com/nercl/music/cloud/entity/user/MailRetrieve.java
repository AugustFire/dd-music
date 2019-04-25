package com.nercl.music.cloud.entity.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mail_retrieves")
public class MailRetrieve implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7924579873014548100L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String email;

	private String sid;

	private Long outTime;

	private String userId;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setOutTime(Long outTime) {
		this.outTime = outTime;
	}

	public Long getOutTime() {
		return outTime;
	}

	public String getId() {
		return id;
	}

}
