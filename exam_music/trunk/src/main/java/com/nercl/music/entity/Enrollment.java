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

import com.nercl.music.entity.user.Examinee;

@Entity
@Table(name = "enrollments")
public class Enrollment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7096050492801969556L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 通过时间
	 */
	@Column
	private Long passAt;

	/**
	 * 生成时间
	 */
	private Long creatAt;

	/**
	 * 审核状态
	 */
	@Enumerated(EnumType.STRING)
	private Status status;

	/**
	 * 不通过原因
	 */
	@Column
	private String unPassedReason;

	/**
	 * 对应考试
	 */
	@Column(name = "exam_id", nullable = false)
	private String examId;

	/**
	 * 对应考试
	 */
	@ManyToOne
	@JoinColumn(name = "exam_id", insertable = false, updatable = false)
	private Exam exam;

	/**
	 * 对应考生
	 */
	@Column(name = "examinee_id", nullable = false)
	private String examineeId;

	/**
	 * 对应考生
	 */
	@ManyToOne
	@JoinColumn(name = "examinee_id", insertable = false, updatable = false)
	private Examinee examinee;

	/**
	 * 对应考场
	 */
	@Column(name = "exam_room_id")
	private String examRoomId;

	/**
	 * 对应考场
	 */
	@ManyToOne
	@JoinColumn(name = "exam_room_id", insertable = false, updatable = false)
	private ExamRoom examRoom;

	public enum Status {

	    /**
	     * 待审核
	     */
		FOR_CHECKED("待审核"),

		/**
		 * 核不通过
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

	public Long getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Long creatAt) {
		this.creatAt = creatAt;
	}

	public Long getPassAt() {
		return passAt;
	}

	public void setPassAt(Long passAt) {
		this.passAt = passAt;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUnPassedReason() {
		return unPassedReason;

	}

	public void setUnPassedReason(String unPassedReason) {
		this.unPassedReason = unPassedReason;
	}

	public String getExamRoomId() {
		return examRoomId;
	}

	public void setExamRoomId(String examRoomId) {
		this.examRoomId = examRoomId;
	}

	public ExamRoom getExamRoom() {
		return examRoom;
	}

	public void setExamRoom(ExamRoom examRoom) {
		this.examRoom = examRoom;
	}

}
