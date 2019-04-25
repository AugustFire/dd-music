package com.nercl.music.cloud.entity;

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

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "activity_resource_relation")
public class ActivityResourceRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8985114138119926320L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 资源id
	 */
	@Column(name = "resource_id")
	private String resourceId;

	/**
	 * 资源类型
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "resource_type")
	private ResourceType resourceType;

	/**
	 * 活动id
	 */
	@Column(name = "activity_id")
	private String activityId;

	@ManyToOne
	@JoinColumn(name = "activity_id", insertable = false, updatable = false)
	private Activity activity;

	public enum ResourceType {
		picture, // 活动图片
		resouce; // 活动其他文件
	}

	public String getId() {
		return id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
