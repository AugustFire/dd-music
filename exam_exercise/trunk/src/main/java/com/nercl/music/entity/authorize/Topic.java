package com.nercl.music.entity.authorize;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "topics")
public class Topic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8307295726256945724L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 专题名称
	 */
	private String title;

	/**
	 * 专题年份
	 */
	private int year;

	/**
	 * 专题地区
	 */
	private String area;

	/**
	 * 专题类型
	 */
	@Enumerated(EnumType.STRING)
	private TopicType topicType;

	/**
	 * 科目类型
	 */
	private Integer subjectType;

	/**
	 * 费用
	 */
	private Integer fee;

	/**
	 * 开始时间
	 */
	private Long startAt;

	/**
	 * 结束时间
	 */
	private Long endAt;

	enum TopicType {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public TopicType getTopicType() {
		return topicType;
	}

	public void setTopicType(TopicType topicType) {
		this.topicType = topicType;
	}

	public String getId() {
		return id;
	}

	public Long getStartAt() {
		return startAt;
	}

	public void setStartAt(Long startAt) {
		this.startAt = startAt;
	}

	public Long getEndAt() {
		return endAt;
	}

	public void setEndAt(Long endAt) {
		this.endAt = endAt;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

}
