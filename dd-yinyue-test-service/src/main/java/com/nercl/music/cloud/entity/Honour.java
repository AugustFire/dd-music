package com.nercl.music.cloud.entity;

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
@Table(name = "honours")
public class Honour implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3698055727344558131L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 荣誉名称
	 */
	private String name;
	/**
	 * 荣誉创建时间
	 */
	@Column(name = "create_time")
	private Long createTime;

	/**
	 * 获得荣誉时间
	 */
	@Column(name = "honour_time")
	private Long honourTime;

	/**
	 * 创建人id
	 */
	@Column(name = "user_id")
	private String userId;

	/**
	 * 学校id
	 */
	@Column(name = "school_id")
	private String schoolId;

	/**
	 * 学校名称
	 */
	@Column(name = "school_name")
	private String schoolName;
	/**
	 * 创建人角色
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;

	/**
	 * 奖项类型
	 * <p>
	 * 演出、比赛、公开课
	 * </p>
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "activity_type")
	private ActivityType activityType;

	/**
	 * 获奖级别
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "award_level")
	private AwardLevel awardLevel;

	/**
	 * 荣誉证书图片id
	 */
	@Column(name = "picture_id")
	private String pictureId;

	public String getId() {
		return id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getHonourTime() {
		return honourTime;
	}

	public void setHonourTime(Long honourTime) {
		this.honourTime = honourTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public AwardLevel getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(AwardLevel awardLevel) {
		this.awardLevel = awardLevel;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPictureId() {
		return pictureId;
	}

	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
