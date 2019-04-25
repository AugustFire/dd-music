package com.nercl.music.cloud.entity.resource;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("topic")
public class TopicResource extends Resource {

	/**
	 * 专题资源
	 */
	private static final long serialVersionUID = 5611969527228863081L;

	/**
	 * 对应专题
	 */
	@Column(name = "topic_id")
	private String topicId;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

}
