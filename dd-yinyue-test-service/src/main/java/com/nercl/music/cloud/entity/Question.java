package com.nercl.music.cloud.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "questions")
public class Question implements Serializable {

	public Question() {
		super();
	}

	public Question(String id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8077819642714861272L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	private Integer year;

	/**
	 * 题干
	 */
	@Lob
	@Column(columnDefinition = "TEXT")
	private String title;

	/**
	 * 题干图片
	 */
	private String titleImage;

	/**
	 * 题干audio
	 */
	private String titleAudio;

	/**
	 * 题目对应的xml文件
	 */
	private String xmlPath;

	/**
	 * 答案对应的模板文件
	 */
	private String templatePath;

	/**
	 * 题干说明
	 */
	@Lob
	@Column(columnDefinition = "TEXT")
	private String explaine;

	/**
	 * 分值
	 */
	@Transient
	private Integer score;

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
	 * 题目类型
	 */
	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	/**
	 * 科目类型
	 */
	@Enumerated(EnumType.STRING)
	private SubjectType subjectType;

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
	@Lob
	@Column(columnDefinition = "TEXT")
	private String analysis;

	/**
	 * 提交入库时间
	 */
	private Long commitedTime;

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

	private Boolean hasStaff;

	/**
	 * 对应习题组
	 */
	@Column(name = "group_id")
	private String groupId;

	/**
	 * 对应习题组
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id", insertable = false, updatable = false)
	private Group group;

	@Enumerated(EnumType.STRING)
	private Dimension dimension;

	private String compositeAbilitys;

	private String knowledges;

	private String grade;

	@Enumerated(EnumType.STRING)
	private VersionDesc version;

	/**
	 * 是否简谱试题
	 */
	private Boolean isNumbered;

	@Enumerated(EnumType.STRING)
	private PresentType presentType;

	@Column(name = "song_id")
	private String songId;

	/**
	 * 是否显示拍号
	 */
	private Boolean isShowTimeSignature;

	private Boolean isChecked;

	private Boolean isDeleted;

	public boolean isCreatingQuestion() {
		return QuestionType.FILL_WORD_CREATING == getQuestionType() || QuestionType.RHYTHM_CREATING == getQuestionType()
				|| QuestionType.MELODY_CREATING == getQuestionType() || QuestionType.SONG_CREATING == getQuestionType()
				|| QuestionType.ASSIGN_MUSIC_CREATING == getQuestionType();
	}

	public boolean isSingQuestion() {
		return QuestionType.SING == getQuestionType() || QuestionType.BEHIND_BACK_SING == getQuestionType()
				|| QuestionType.PERFORMANCE == getQuestionType()
				|| QuestionType.BEHIND_BACK_PERFORMANCE == getQuestionType()
				|| QuestionType.SIGHT_SINGING == getQuestionType();
	}

	public boolean isSelectQuestion() {
		return QuestionType.SINGLE_SELECT == getQuestionType() || QuestionType.MULTI_SELECT == getQuestionType();
	}

	@Override
	public boolean equals(Object another) {
		if (null == another) {
			return false;
		}
		if (!(another instanceof Question)) {
			return false;
		}
		Question question = (Question) another;
		return this.getId().equals(question.getId());
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + getId().hashCode();
		return hash;
	}

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

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
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

	public SubjectType getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(SubjectType subjectType) {
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

	public Boolean getHasStaff() {
		return hasStaff;
	}

	public void setHasStaff(Boolean hasStaff) {
		this.hasStaff = hasStaff;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getCompositeAbilitys() {
		return compositeAbilitys;
	}

	public void setCompositeAbilitys(String compositeAbilitys) {
		this.compositeAbilitys = compositeAbilitys;
	}

	public String getKnowledges() {
		return knowledges;
	}

	public void setKnowledges(String knowledges) {
		this.knowledges = knowledges;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public VersionDesc getVersion() {
		return version;
	}

	public void setVersion(VersionDesc version) {
		this.version = version;
	}

	public Boolean getIsNumbered() {
		return isNumbered;
	}

	public void setIsNumbered(Boolean isNumbered) {
		this.isNumbered = isNumbered;
	}

	public PresentType getPresentType() {
		return presentType;
	}

	public void setPresentType(PresentType presentType) {
		this.presentType = presentType;
	}

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public Boolean getIsShowTimeSignature() {
		return isShowTimeSignature;
	}

	public void setIsShowTimeSignature(Boolean isShowTimeSignature) {
		this.isShowTimeSignature = isShowTimeSignature;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
