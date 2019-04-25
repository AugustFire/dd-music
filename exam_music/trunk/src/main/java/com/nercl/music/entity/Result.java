package com.nercl.music.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.entity.user.Examinee;

@Entity
@Table(name = " results")
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150711012167343926L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 年度
	 */
	private Integer year;

	/**
	 * 分值
	 */
	private Integer score;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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

}
