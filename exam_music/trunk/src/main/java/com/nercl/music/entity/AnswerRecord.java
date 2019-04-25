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
@Table(name = "answer_records")
public class AnswerRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8536728273422930852L;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(String examPaperId) {
		this.examPaperId = examPaperId;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public ExamPaper getExamPaper() {
		return examPaper;
	}

	public void setExamPaper(ExamPaper examPaper) {
		this.examPaper = examPaper;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public Integer getScore() {
		return null == score ? 0 : score;
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

}
