package com.nercl.music.entity.user;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("ExamineeGroup")
public class ExamineeGroup extends AbstractGroup {

	/**
	 * 考生分组
	 */
	@ManyToMany(mappedBy = "examineeGroups")
	private List<ExpertGroup> expertGroups;

	public List<ExpertGroup> getExpertGroups() {
		return expertGroups;
	}

	public void setExpertGroups(List<ExpertGroup> expertGroups) {
		this.expertGroups = expertGroups;
	}

}
