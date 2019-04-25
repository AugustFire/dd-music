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
@Table(name = "activities")
public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3472723862929976059L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 活动名称
	 * */
	private String name;
	/**
	 * 活动创建时间
	 */
	@Column(name = "create_time")
	private Long createTime;

	/**
	 * 活动开始时间
	 */
	@Column(name = "start_time")
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	@Column(name = "end_time")
	private Long endTime;

	/**
	 * 活动创建人id
	 */
	@Column(name = "user_id")
	private String userId;
	
	/**
	 * 活动创建人角色
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private UserRole userRole;
	
	/**
	 * 创建人名
	 * */
	@Column(name = "user_name")
	private String userName;

	/**
	 * 活动审批状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "check_status")
	private CheckStatus checkStatus;

	/**
	 * 艺术活动类型
	 * <p>
	 * 老师-演出、比赛、公开课
	 * </p>
	 * <p>
	 * 学生-音乐考级、比赛获奖
	 * </p>
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "activity_type")
	private ActivityType activityType;

	/**
	 * 考试/比赛项目
	 */
	private String item;

	/**
	 * 获奖情况
	 */
	private String award;

	/**
	 * 获奖等级
	 */
	private String level;

	/**
	 * 获奖级别
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "award_level")
	private AwardLevel awardLevel;

	public String getId() {
		return id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public CheckStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
