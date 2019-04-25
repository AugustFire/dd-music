package com.nercl.music.cloud.entity.ability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("grade_composite_ability_statistic")
public class GradeCompositeAbilityStatistic extends Statistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368742603871106107L;

	private String gradeId;

	private String gradeName;

	private String schoolId;

	private String schoolName;

	private String compositeAbility;

	public GradeCompositeAbilityStatistic() {
	}

	public GradeCompositeAbilityStatistic(Long startAt, Long endAt, String gradeId, String gradeName, String schoolId,
			String schoolName, String compositeAbility, Integer grasp) {
		super(startAt, endAt, grasp);
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.compositeAbility = compositeAbility;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
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
