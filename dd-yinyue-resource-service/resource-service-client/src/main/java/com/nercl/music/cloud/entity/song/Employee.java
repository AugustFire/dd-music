//package com.nercl.music.cloud.entity.song;
//
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//
////indexName ：索引名字（对应mysql的数据库名字）
////type:类型（对应mysql的表名）
//@Document(indexName = "megacorp", type = "employee", shards = 1, replicas = 1, refreshInterval = "-1")
//public class Employee {
//
//	@Id
//	private String id;
//
//	@Field
//	private String firstName;
//
//	@Field
//	private String lastName;
//
//	@Field
//	private Integer age = 0;
//
//	@Field
//	private String about;
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public Integer getAge() {
//		return age;
//	}
//
//	public void setAge(Integer age) {
//		this.age = age;
//	}
//
//	public String getAbout() {
//		return about;
//	}
//
//	public void setAbout(String about) {
//		this.about = about;
//	}
//}
