package com.nercl.music.cloud.entity.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 年级
 * 
 */
@Entity
@Table(name = "grades", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "code", "learn_stage_id" }) })
public class Grade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8392716270588368156L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 年级名称
	 */
	private String name;

	/**
	 * 年级代码
	 */
	private String code;

	/**
	 * 学段
	 */
	@Column(name = "learn_stage_id")
	private String learnStageId;

	/**
	 * 学段
	 */
	@ManyToOne
	@JoinColumn(name = "learn_stage_id", insertable = false, updatable = false)
	private LearnStage learnStage;

	@Override
	public boolean equals(Object another) {
		if (null == another) {
			return false;
		}
		if (!(another instanceof Grade)) {
			return false;
		}
		Grade grade = (Grade) another;
		return this.getId().equals(grade.getId());
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + getId().hashCode();
		return hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public LearnStage getLearnStage() {
		return learnStage;
	}

	public void setLearnStage(LearnStage learnStage) {
		this.learnStage = learnStage;
	}

	public String getLearnStageId() {
		return learnStageId;
	}

	public void setLearnStageId(String learnStageId) {
		this.learnStageId = learnStageId;
	}
}
