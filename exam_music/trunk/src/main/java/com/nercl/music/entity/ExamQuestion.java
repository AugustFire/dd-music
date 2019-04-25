package com.nercl.music.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "exam_questions")
public class ExamQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8077819642714861272L;

	@Id
	private String id;

	/**
	 * 题干
	 */
	private String title;

	@Transient
	private String title2;

	/**
	 * 题干图片
	 */
	private String titleImage;

	/**
	 * 题干audio
	 */
	private String titleAudio;

	/**
	 * 题干说明
	 */
	private String explaine;

	/**
	 * 分值
	 */
	private Integer score;

	@Transient
	private Integer score2;

	/**
	 * 准备时长
	 */
	private Integer prepareTime;

	/**
	 * 规定答题时长
	 */
	private Integer limitTime;

	/**
	 * 调式
	 */
	private Integer tune;

	/**
	 * 速度
	 */
	private Integer tempo;

	/**
	 * 题目对应的xml文件
	 */
	private String xmlPath;

	/**
	 * 题目对应的xml文件
	 */
	@Transient
	private String xmlPath2;

	/**
	 * 题目类型
	 */
	private Integer questionType;

	/**
	 * 科目类型
	 */
	private Integer subjectType;

	/**
	 * 考点/考试内容
	 */
	private Integer examField;

	/**
	 * 值0-1,0表示最难，1表示最简单
	 */
	private Float difficulty;

	/**
	 * 信度0-1
	 */
	private Float reliability;

	/**
	 * 效度0-1
	 */
	private Float validity;

	/**
	 * 区分度-1-1
	 */
	private Float discrimination;

	/**
	 * 保密等级
	 */
	private Integer secretLevel;

	/**
	 * 是否公开
	 */
	@Column(nullable = true)
	private Boolean isOpen;

	/**
	 * 预估解题时间
	 */
	private Integer estimatetime;

	/**
	 * 题目解析
	 */
	private String analysis;

	/**
	 * 提交入库时间
	 */
	private Long commitedTime;

	/**
	 * 知识点
	 */
	private String knowledge;

	/**
	 * 是否播放标准音
	 */
	@Column(nullable = true)
	private Boolean isPlayStandardNote;

	/**
	 * 是否播放预备拍
	 */
	@Column(nullable = true)
	private Boolean isPlayRepareBeat;

	/**
	 * 是否播放主和弦
	 */
	@Column(nullable = true)
	private Boolean isPlayMainChord;

	/**
	 * 是否播放调试音阶
	 */
	@Column(nullable = true)
	private Boolean isPlayDebugPitch;

	/**
	 * 是否播放起始音
	 */
	@Column(nullable = true)
	private Boolean isPlayStartNote;

	/**
	 * 听音音程或和弦性质组数
	 */
	private Integer groupnum;

	/**
	 * 听音：播放次数
	 */
	private Integer playnum;

	/**
	 * 循环播放等待时间，以秒为单位
	 */
	private Integer delay;

	/**
	 * 小节数(简答题使用)
	 */
	private Integer measurenum;

	/**
	 * 节拍分子,默认为4
	 */
	private Integer numerator;

	/**
	 * 节拍分母，默认为4
	 */
	private Integer denominator;

	@OneToMany(mappedBy = "examQuestion", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private List<Option> options;

	private String unPassReason;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private CheckRecord.Status checkStatus;

	/**
	 * 对应答案
	 */
	@OneToOne(mappedBy = "examQuestion")
	private Answer answer;

	private Boolean hasStaff;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Float getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Float difficulty) {
		this.difficulty = difficulty;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Float getReliability() {
		return reliability;
	}

	public void setReliability(Float reliability) {
		this.reliability = reliability;
	}

	public Float getValidity() {
		return validity;
	}

	public void setValidity(Float validity) {
		this.validity = validity;
	}

	public Float getDiscrimination() {
		return discrimination;
	}

	public void setDiscrimination(Float discrimination) {
		this.discrimination = discrimination;
	}

	public Integer getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}

	public Integer getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	public Integer getTune() {
		return tune;
	}

	public void setTune(Integer tune) {
		this.tune = tune;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public String getTitleAudio() {
		return titleAudio;
	}

	public void setTitleAudio(String titleAudio) {
		this.titleAudio = titleAudio;
	}

	public String getExplaine() {
		return explaine;
	}

	public void setExplaine(String explaine) {
		this.explaine = explaine;
	}

	public Integer getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(Integer prepareTime) {
		this.prepareTime = prepareTime;
	}

	public Integer getTempo() {
		return tempo;
	}

	public void setTempo(Integer tempo) {
		this.tempo = tempo;
	}

	public Boolean getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getExamField() {
		return examField;
	}

	public void setExamField(Integer examField) {
		this.examField = examField;
	}

	public Long getCommitedTime() {
		return commitedTime;
	}

	public void setCommitedTime(Long commitedTime) {
		this.commitedTime = commitedTime;
	}

	public Integer getEstimatetime() {
		return estimatetime;
	}

	public void setEstimatetime(Integer estimatetime) {
		this.estimatetime = estimatetime;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	public Boolean getIsPlayStandardNote() {
		return isPlayStandardNote;
	}

	public void setIsPlayStandardNote(Boolean isPlayStandardNote) {
		this.isPlayStandardNote = isPlayStandardNote;
	}

	public Boolean getIsPlayRepareBeat() {
		return isPlayRepareBeat;
	}

	public void setIsPlayRepareBeat(Boolean isPlayRepareBeat) {
		this.isPlayRepareBeat = isPlayRepareBeat;
	}

	public Boolean getIsPlayMainChord() {
		return isPlayMainChord;
	}

	public void setIsPlayMainChord(Boolean isPlayMainChord) {
		this.isPlayMainChord = isPlayMainChord;
	}

	public Boolean getIsPlayDebugPitch() {
		return isPlayDebugPitch;
	}

	public void setIsPlayDebugPitch(Boolean isPlayDebugPitch) {
		this.isPlayDebugPitch = isPlayDebugPitch;
	}

	public Boolean getIsPlayStartNote() {
		return isPlayStartNote;
	}

	public void setIsPlayStartNote(Boolean isPlayStartNote) {
		this.isPlayStartNote = isPlayStartNote;
	}

	public Integer getGroupnum() {
		return groupnum;
	}

	public void setGroupnum(Integer groupnum) {
		this.groupnum = groupnum;
	}

	public Integer getPlaynum() {
		return playnum;
	}

	public void setPlaynum(Integer playnum) {
		this.playnum = playnum;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public CheckRecord.Status getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CheckRecord.Status checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getUnPassReason() {
		return unPassReason;
	}

	public void setUnPassReason(String unPassReason) {
		this.unPassReason = unPassReason;
	}

	public Integer getMeasurenum() {
		return measurenum;
	}

	public void setMeasurenum(Integer measurenum) {
		this.measurenum = measurenum;
	}

	public Integer getNumerator() {
		return numerator;
	}

	public void setNumerator(Integer numerator) {
		this.numerator = numerator;
	}

	public Integer getDenominator() {
		return denominator;
	}

	public void setDenominator(Integer denominator) {
		this.denominator = denominator;
	}

	public String getXmlPath2() {
		return xmlPath2;
	}

	public void setXmlPath2(String xmlPath2) {
		this.xmlPath2 = xmlPath2;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public Boolean getHasStaff() {
		return hasStaff;
	}

	public void setHasStaff(Boolean hasStaff) {
		this.hasStaff = hasStaff;
	}

	public Integer getScore2() {
		return score2;
	}

	public void setScore2(Integer score2) {
		this.score2 = score2;
	}

}
