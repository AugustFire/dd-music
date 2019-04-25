package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.user.Examinee;
import com.nercl.music.entity.user.Expert;

@Entity
@Table(name = "expert_results")
public class ExpertResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7888839156833697517L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 对应考试
	 */
	@Column(name = "exam_id")
	private String examId;

	/**
	 * 对应考试
	 */
	@ManyToOne
	@JoinColumn(name = "exam_id", insertable = false, updatable = false)
	private Exam exam;

	/**
	 * 对应考卷
	 */
	@Column(name = "exam_paper_id")
	private String examPaperId;

	/**
	 * 对应考卷
	 */
	@ManyToOne
	@JoinColumn(name = "exam_paper_id", insertable = false, updatable = false)
	private ExamPaper examPaper;

	/**
	 * 对应专家
	 */
	@Column(name = "expert_id")
	private String expertId;

	/**
	 * 对应专家
	 */
	@ManyToOne
	@JoinColumn(name = "expert_id", insertable = false, updatable = false)
	private Expert expert;

	/**
	 * 对应考题
	 */
	@Column(name = "question_id")
	private String examQuestionId;

	/**
	 * 对应考题
	 */
	@ManyToOne
	@JoinColumn(name = "question_id", insertable = false, updatable = false)
	private ExamQuestion examQuestion;

	/**
	 * 对应考生
	 */
	@Column(name = "examinee_id")
	private String examineeId;

	/**
	 * 对应考生
	 */
	@ManyToOne
	@JoinColumn(name = "examinee_id", insertable = false, updatable = false)
	private Examinee examinee;

	/**
	 * 分值
	 */
	private Integer score;

	/**
	 * 评论
	 */
	private String comment;

	@Transient
	private String qtitle;

	@Transient
	private Double avgScore;

	@Transient
	private Integer mscore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getExpertId() {
		return expertId;
	}

	public void setExpertId(String expertId) {
		this.expertId = expertId;
	}

	public Expert getExpert() {
		return expert;
	}

	public void setExpert(Expert expert) {
		this.expert = expert;
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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getExamineeId() {
		return examineeId;
	}

	public void setExamineeId(String examineeId) {
		this.examineeId = examineeId;
	}

	public Examinee getExaminee() {
		return examinee;
	}

	public void setExaminee(Examinee examinee) {
		this.examinee = examinee;
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

	public String getQtitle() {
		return qtitle;
	}

	public void setQtitle(String qtitle) {
		this.qtitle = qtitle;
	}

	public Double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}

	public Integer getMscore() {
		return mscore;
	}

	public void setMscore(Integer mscore) {
		this.mscore = mscore;
	}

}
