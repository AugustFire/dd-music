package com.nercl.music.entity.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "persons")
public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3659238264886756117L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 姓名
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 邮箱
	 */
	@Column(nullable = false)
	private String email;

	/**
	 * 联系电话
	 */
	@Column(nullable = false)
	private String phone;

	/**
	 * 性别
	 */
	@Enumerated(EnumType.STRING)
	private Gender gender;

	/**
	 * 地址
	 */
	private String address;

	public enum Gender {

	    /**
	     * 男
	     */
		MAN("男"),

		/**
		 * 女
		 */
		WOMAN("女");

		private String desc;
	
		private Gender(String desc) {
			this.desc = desc;
		}
	
		public String getDesc() {
			return desc;
		}
	
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
