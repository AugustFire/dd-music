package com.nercl.music.cloud.entity.ability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("class_composite_ability_statistic")
public class ClassCompositeAbilityStatistic extends Statistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7368742603871106107L;

	private String classId;

	private String className;

	private String gradeId;

	private String gradeName;

	private String compositeAbility;
	
	public ClassCompositeAbilityStatistic() {
	}

	public ClassCompositeAbilityStatistic(Long startAt, Long endAt, String classId, String className, String gradeId,
			String gradeName, String compositeAbility, Integer grasp) {
		super(startAt, endAt, grasp);
		this.classId = classId;
		this.className = className;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.compositeAbility = compositeAbility;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCompositeAbility() {
		return compositeAbility;
	}

	public void setCompositeAbility(String compositeAbility) {
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

}
