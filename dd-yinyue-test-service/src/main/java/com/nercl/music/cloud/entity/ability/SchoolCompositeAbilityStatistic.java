package com.nercl.music.cloud.entity.ability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("school_composite_ability_statistic")
public class SchoolCompositeAbilityStatistic extends Statistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368742603871106107L;

	private String schoolId;

	private String schoolName;

	private String compositeAbility;
	
	public SchoolCompositeAbilityStatistic() {
	}

	public SchoolCompositeAbilityStatistic(Long startAt, Long endAt, String schoolId, String schoolName,
			String compositeAbility, Integer grasp) {
		super(startAt, endAt, grasp);
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.compositeAbility = compositeAbility;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCompositeAbility() {
		return compositeAbility;
	}

	public void setCompositeAbility(String compositeAbility) {
		this.compositeAbility = compositeAbility;
	}

}
