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
@Table(name = "group_users")
public class GroupUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2932084529235326200L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应学生
	 */
	private String userId;

	/**
	 * 是否组长
	 */
	@Column(name = "is_leader")
	private Boolean isLeader;

	/**
	 * 对应组
	 */
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 对应组
	 */
	@ManyToOne
	@JoinColumn(name = "group_id", insertable = false, updatable = false)
	private Group group;

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

	public Boolean getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Boolean isLeader) {
		this.isLeader = isLeader;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

}
