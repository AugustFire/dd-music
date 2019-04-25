package com.nercl.music.cloud.entity.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.classroom.ClassRoom;

@Entity
@Table(name = "groups")
public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -73863837141423761L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String title;

	/**
	 * 对应活动
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 对应活动
	 */
	@ManyToOne
	@JoinColumn(name = "activity_id", insertable = false, updatable = false)
	private Activity activity;

	/**
	 * 对应课堂
	 */
	@Column(name = "class_room_id")
	private String classRoomId;

	/**
	 * 对应课堂
	 */
	@ManyToOne
	@JoinColumn(name = "class_room_id", insertable = false, updatable = false)
	private ClassRoom classRoom;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public ClassRoom getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(ClassRoom classRoom) {
		this.classRoom = classRoom;
	}

	public String getId() {
		return id;
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
