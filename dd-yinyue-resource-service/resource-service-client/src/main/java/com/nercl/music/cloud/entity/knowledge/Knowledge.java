
package com.nercl.music.cloud.entity.knowledge;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "knowledges")
public class Knowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424070808049049449L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String no;

	private String title;

	/**
	 * 难度系数
	 */
	private Float difficulty;

	/**
	 * 父级
	 */
	@Column(name = "parent_id")
	private String parentId;

	/**
	 * 父级
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private Knowledge parent;

	/**
	 * 适用领域
	 */
	@Enumerated(EnumType.STRING)
	private SuitField suitField;

	public enum SuitField {

		/**
		 * 中小学
		 */
		MIDDLE_PRIMARY_SCHOOL,

		/**
		 * 大学
		 */
		UNIVERSITY,

		/**
		 * 培训机构
		 */
		TRAINING_ORGANIZATION;

	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Float difficulty) {
		this.difficulty = difficulty;
	}

	public String getId() {
		return id;
	}

	public Knowledge getParent() {
		return parent;
	}

	public void setParent(Knowledge parent) {
		this.parent = parent;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public SuitField getSuitField() {
		return suitField;
	}

	public void setSuitField(SuitField suitField) {
		this.suitField = suitField;
	}
}
