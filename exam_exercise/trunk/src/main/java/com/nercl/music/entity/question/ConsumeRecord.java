package com.nercl.music.entity.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.user.Person;

@Entity
@Table(name = "consume_records")
public class ConsumeRecord implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1074244036445355328L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 开始时间
	 */
	private Long startAt;

	/**
	 * 结束时间
	 */
	private Long endtAt;

	/**
	 * 是否结束
	 */
	private Boolean ended;

	/**
	 * 持续时间,单位为秒
	 */
	private Long duration;

	/**
	 * 对应练习者
	 */
	@Column(name = "exerciser_id")
	private String exerciserId;

	/**
	 * 对应练习者
	 */
	@ManyToOne
	@JoinColumn(name = "exerciser_id", insertable = false, updatable = false)
	private Person exerciser;

	public Long getStartAt() {
		return startAt;
	}

	public void setStartAt(Long startAt) {
		this.startAt = startAt;
	}

	public Long getEndtAt() {
		return endtAt;
	}

	public void setEndtAt(Long endtAt) {
		this.endtAt = endtAt;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getExerciserId() {
		return exerciserId;
	}

	public void setExerciserId(String exerciserId) {
		this.exerciserId = exerciserId;
	}

	public Person getExerciser() {
		return exerciser;
	}

	public void setExerciser(Person exerciser) {
		this.exerciser = exerciser;
	}

	public String getId() {
		return id;
	}

	public Boolean getEnded() {
		return ended;
	}

	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

}
