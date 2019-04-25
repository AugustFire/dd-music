package com.nercl.music.cloud.entity.ability;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "statistics")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 255)
@DiscriminatorValue("statistics")
public class Statistic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5159268237672227023L;

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

	private Integer grasp;

	public Statistic() {
	}

	public Statistic(Long startAt, Long endAt, Integer grasp) {
		this.startAt = startAt;
		this.endAt = endAt;
		this.grasp = grasp;
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

	public String getId() {
		return id;
	}

	public Integer getGrasp() {
		return grasp;
	}

	public void setGrasp(Integer grasp) {
		this.grasp = grasp;
	}

}
