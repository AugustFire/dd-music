package com.nercl.music.entity.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@DiscriminatorValue("ExpertGroup")
public class ExpertGroup extends AbstractGroup {

	/**
	 * 考生分组
	 */
	@ManyToMany(targetEntity = AbstractGroup.class, cascade = { CascadeType.ALL })
	@JoinTable(name = "examinee_expert_groups", joinColumns = {
	        @JoinColumn(name = "expert_group_id") }, inverseJoinColumns = { @JoinColumn(name = "examinee_group_id") })
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExamineeGroup> examineeGroups;

	public List<ExamineeGroup> getExamineeGroups() {
		return examineeGroups;
	}

	public void setExamineeGroups(List<ExamineeGroup> examineeGroups) {
		this.examineeGroups = examineeGroups;
	}

}
