package com.nercl.music.entity.authorize;

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
@Table(name = "authorize_records")
public class AuthorizeRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8523609265275058108L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 生成时间
	 */
	private Long creatAt;

	/**
	 * 对应专题
	 */
	@Column(name = "topic_id")
	private String topicId;

	/**
	 * 对应专题
	 */
	@ManyToOne
	@JoinColumn(name = "topic_id", insertable = false, updatable = false)
	private Topic topic;

	/**
	 * 授权人
	 */
	@Column(name = "authorizer_id")
	private String authorizerId;

	/**
	 * 授权人
	 */
	@ManyToOne
	@JoinColumn(name = "authorizer_id", insertable = false, updatable = false)
	private Person authorizer;

	/**
	 * 被授权人
	 */
	@Column(name = "to_authorizer_id")
	private String toAuthorizerId;

	/**
	 * 被授权人
	 */
	@ManyToOne
	@JoinColumn(name = "to_authorizer_id", insertable = false, updatable = false)
	private Person toAuthorizer;

	public Long getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Long creatAt) {
		this.creatAt = creatAt;
	}

	public Person getAuthorizer() {
		return authorizer;
	}

	public void setAuthorizer(Person authorizer) {
		this.authorizer = authorizer;
	}

	public Person getToAuthorizer() {
		return toAuthorizer;
	}

	public void setToAuthorizer(Person toAuthorizer) {
		this.toAuthorizer = toAuthorizer;
	}

	public String getId() {
		return id;
	}

	public String getAuthorizerId() {
		return authorizerId;
	}

	public void setAuthorizerId(String authorizerId) {
		this.authorizerId = authorizerId;
	}

	public String getToAuthorizerId() {
		return toAuthorizerId;
	}

	public void setToAuthorizerId(String toAuthorizerId) {
		this.toAuthorizerId = toAuthorizerId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
