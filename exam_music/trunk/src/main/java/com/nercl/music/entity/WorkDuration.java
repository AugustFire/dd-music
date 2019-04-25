package com.nercl.music.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "work_durations")
public class WorkDuration {

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private Integer year;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	private String remark;

	@Enumerated(EnumType.STRING)
	private WorkUnit workUnit;

	public enum WorkUnit {

	    /**
	     * 报名
	     */
		ENROLLMENT(1),

		/**
		 * 设置专家
		 */
		EXPERT_SETTING(2),

		/**
		 * 专家打分
		 */
		EXPERT_MARKED(3),

		/**
		 * 成绩查询
		 */
		RESULT_GET(4);

		private WorkUnit(int order) {
			this.order = order;
		}

		private int order;

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public WorkUnit getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(WorkUnit workUnit) {
		this.workUnit = workUnit;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
