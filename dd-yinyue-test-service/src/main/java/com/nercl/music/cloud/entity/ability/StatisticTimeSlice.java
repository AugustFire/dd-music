package com.nercl.music.cloud.entity.ability;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "statistic_time_slices")
public class StatisticTimeSlice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 769491437743866787L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 开始时间
	 */
	private Long startAt;

	/**
	 * 结束时间
	 */
	private Long endAt;

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

	public String getId() {
		return id;
	}

}
