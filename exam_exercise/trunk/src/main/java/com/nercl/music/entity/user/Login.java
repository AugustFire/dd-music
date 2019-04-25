package com.nercl.music.entity.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "logins")
public class Login implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5074281001868119508L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 登录名
	 */
	@Column(nullable = false, unique = true)
	private String login;

	/**
	 * 密码
	 */
	@Transient
	private String password;

	/**
	 * 密码
	 */
	@Column(nullable = false)
	private String encryptedPassword;

	/**
	 * 秘钥
	 */
	@Column(nullable = false)
	private String salt;

	/**
	 * 生成时间
	 */
	private Long createAt;

	/**
	 * 最近登录时间
	 */
	private Long lastLoginTime;

	/**
	 * 角色
	 */
	@Enumerated(EnumType.STRING)
	private Role role;

	/**
	 * 账户所有者
	 */
	@Column(name = "person_id", nullable = false)
	private String personId;

	/**
	 * 账户所有者
	 */
	@ManyToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;

	/**
	 * 登录码，用于防止一个账号多地方登录
	 */
	private String loginToken;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Long createAt) {
		this.createAt = createAt;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

}
