package com.nercl.music.cloud.entity.base;

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
@Table(name = "schools")
public class School implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7690100759105046155L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String code;

	@Column(nullable = false)
	private String name;

	@Column(name = "region_id")
	private String regionId;


	@Column(name = "city_id")
	private String cityId;

	@Column(name = "learn_stage_id")
	private String learnStageId;
	
	@ManyToOne
	@JoinColumn(name = "learn_stage_id", insertable = false, updatable = false)
	private LearnStage learnStage;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getId() {
		return id;
	}

	public String getLearnStageId() {
		return learnStageId;
	}

	public void setLearnStageId(String learnStageId) {
		this.learnStageId = learnStageId;
	}

	public LearnStage getLearnStage() {
		return learnStage;
	}

	public void setLearnStage(LearnStage learnStage) {
		this.learnStage = learnStage;
	}

}
