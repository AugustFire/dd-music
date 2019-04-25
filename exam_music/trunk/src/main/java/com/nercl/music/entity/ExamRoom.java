package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "exam_rooms")
public class ExamRoom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8670817742454450673L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 名字
	 */
	private String title;

	/**
	 * 对应考点
	 */
	@Column(name = "exam_point_id")
	private String examPointId;

	/**
	 * 对应考点
	 */
	@ManyToOne
	@JoinColumn(name = "exam_point_id", insertable = false, updatable = false)
	private ExamPoint examPoint;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExamPointId() {
		return examPointId;
	}

	public void setExamPointId(String examPointId) {
		this.examPointId = examPointId;
	}

	public ExamPoint getExamPoint() {
		return examPoint;
	}

	public void setExamPoint(ExamPoint examPoint) {
		this.examPoint = examPoint;
	}

}
