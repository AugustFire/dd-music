package com.nercl.music.cloud.entity.ability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("school_knowledge_statistic")
public class SchoolKnowledgeStatistic extends Statistic {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1165347321336242933L;

	private String schoolId;

	private String schoolName;

	private String knowledge;

	private String knowledgeNo;
	
	public SchoolKnowledgeStatistic() {
	}

	public SchoolKnowledgeStatistic(Long startAt, Long endAt, String schoolId, String schoolName, String knowledge,
			String knowledgeNo, Integer grasp) {
		super(startAt, endAt, grasp);
		this.schoolId = schoolId;
		this.schoolName = schoolName;
		this.knowledge = knowledge;
		this.knowledgeNo = knowledgeNo;
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

}
