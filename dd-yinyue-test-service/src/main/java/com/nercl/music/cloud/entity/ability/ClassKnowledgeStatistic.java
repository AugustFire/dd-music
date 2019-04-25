package com.nercl.music.cloud.entity.ability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("class_knowledge_statistic")
public class ClassKnowledgeStatistic extends Statistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1165347321336242933L;

	private String classId;

	private String className;

	private String gradeId;

	private String gradeName;

	private String knowledge;

	private String knowledgeNo;

	public ClassKnowledgeStatistic() {
	}

	public ClassKnowledgeStatistic(Long startAt, Long endAt, String classId, String className, String gradeId,
			String gradeName, String knowledge, String knowledgeNo, Integer grasp) {
		super(startAt, endAt, grasp);
		this.classId = classId;
		this.className = className;
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.knowledge = knowledge;
		this.knowledgeNo = knowledgeNo;
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

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	public String getKnowledgeNo() {
		return knowledgeNo;
	}

	public void setKnowledgeNo(String knowledgeNo) {
		this.knowledgeNo = knowledgeNo;
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
