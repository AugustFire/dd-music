package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "answer_records")
public class AnswerRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4354345293281839469L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private String userId;

	private String userName;

	private String classId;

	private String className;

	private String gradeId;

	private String gradeName;

	private String schoolId;

	private String schoolName;

	/**
	 * 对应问题
	 */
	@Column(name = "question_id")
	private String questionId;

	/**
	 * 对应问题
	 */
	@ManyToOne
	@JoinColumn(name = "question_id", insertable = false, updatable = false)
	private Question question;

	/**
	 * 多选题答案用“,”分隔
	 */
	private String answer;

	/**
	 * 对应资源
	 */
	private String resourceId;

	/**
	 * 对应课堂
	 */
	@Column(name = "class_room_id")
	private String classRoomId;

	@Column(name = "timestamp")
	private Long timestamp;

	private Boolean isTrue;

	@Column(name = "score", columnDefinition = "float(10,2)")
	private Float score;

	@Column(name = "full_score", columnDefinition = "float(10,2)")
	private Float fullScore;

	/**
	 * 教师对答案的点评
	 */
	@Lob
	@Column(columnDefinition = "TEXT")
	private String comment;

	private String taskId;

	private String chapterId;

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

	@Enumerated(EnumType.STRING)
	private AnswerSource answerSource;

	private Integer tempo;

	@Override
	public boolean equals(Object another) {
		if (null == another) {
			return false;
		}
		if (!(another instanceof AnswerRecord)) {
			return false;
		}
		AnswerRecord anotherAnswerRecord = (AnswerRecord) another;
		if (this.getId().equals(anotherAnswerRecord.getId())
				&& this.getQuestionId() == anotherAnswerRecord.getQuestionId()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + getId().hashCode();
		hash = hash * 31 + getQuestionId().hashCode();
		return hash;
	}

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsTrue() {
		return isTrue;
	}

	public void setIsTrue(Boolean isTrue) {
		this.isTrue = isTrue;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Float getFullScore() {
		return fullScore;
	}

	public void setFullScore(Float fullScore) {
		this.fullScore = fullScore;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public AnswerSource getAnswerSource() {
		return answerSource;
	}

	public void setAnswerSource(AnswerSource answerSource) {
		this.answerSource = answerSource;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

}
