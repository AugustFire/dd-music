package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.user.Person;

@Entity
@Table(name = "check_records")
public class CheckRecord implements Serializable {

	private static final long serialVersionUID = 7226904786554595088L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 审核时间
	 */
	private long checkAt;

	/**
	 * 对应审核人
	 */
	@Column(name = "check_user_id", nullable = true)
	private String checkUserId;

	/**
	 * 对应审核人
	 */
	@ManyToOne
	@JoinColumn(name = "check_user_id", insertable = false, updatable = false)
	private Person checkUser;

	@Enumerated(EnumType.STRING)
	private Status status;

	private String unPassReason;

	/**
	 * 对应考卷
	 */
	@Column(name = "exam_paper_id", nullable = true)
	private String examPaperId;

	/**
	 * 对应考卷
	 */
	@ManyToOne
	@JoinColumn(name = "exam_paper_id", insertable = false, updatable = false)
	private ExamPaper examPaper;

	/**
	 * 对应考题
	 */
	@Column(name = "exam_question_id", nullable = true)
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "exam_question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	public enum Status {

	    /**
	     * 待审核
	     */
		FOR_CHECKED("待审核"),

		/**
		 * 此表不需要落待审核状态记录，所有试题第一次上传后默认都是待审核状态，不需要都往这张表里插数据，只有审核操作过后才会落记录 核不通过
		 */
		UN_PASSED("未通过"),

		/**
		 * 已审核
		 */
		PASSED("已通过");

		private String desc;

		private Status(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCheckAt() {
		return checkAt;
	}

	public void setCheckAt(long checkAt) {
		this.checkAt = checkAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUnPassReason() {
		return unPassReason;
	}

	public void setUnPassReason(String unPassReason) {
		this.unPassReason = unPassReason;
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public Person getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(Person checkUser) {
		this.checkUser = checkUser;
	}

	public String getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(String examPaperId) {
		this.examPaperId = examPaperId;
	}

	public ExamPaper getExamPaper() {
		return examPaper;
	}

	public void setExamPaper(ExamPaper examPaper) {
		this.examPaper = examPaper;
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

}
