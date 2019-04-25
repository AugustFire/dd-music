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

import com.nercl.music.entity.authorize.Topic;
import com.nercl.music.entity.user.Person;

@Entity
@Table(name = "exerciser_answer_records")
public class ExerciserAnswerRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8896427319429398997L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 生成时间
	 */
	private long creatAt;

	/**
	 * 答案内容
	 */
	private String content;

	/**
	 * 对应资源路径
	 */
	private String resPath;

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
	 * 对应练习者
	 */
	@Column(name = "person_id")
	private String personId;

	/**
	 * 对应练习者
	 */
	@ManyToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;

	/**
	 * 对应考题
	 */
	@Column(name = "exam_question_id")
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "exam_question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	/**
	 * 考题类型
	 */
	private Integer questionType;

	/**
	 * 准确率
	 */
	private Integer accuracy;

	/**
	 * 分数
	 */
	private Integer score;

	private Boolean isParsed;

	public long getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(long creatAt) {
		this.creatAt = creatAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getResPath() {
		return resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getExamQuestionId() {
		return examQuestionId;
	}

	public void setExamQuestionId(String examQuestionId) {
		this.examQuestionId = examQuestionId;
	}

	public ExamQuestion getExamQuestion() {
		return examQuestion;
	}

	public void setExamQuestion(ExamQuestion examQuestion) {
		this.examQuestion = examQuestion;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getId() {
		return id;
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

	public Boolean getIsParsed() {
		return isParsed;
	}

	public void setIsParsed(Boolean isParsed) {
		this.isParsed = isParsed;
	}

}
