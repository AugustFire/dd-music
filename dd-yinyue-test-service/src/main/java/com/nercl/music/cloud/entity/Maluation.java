package com.nercl.music.cloud.entity;

public enum Maluation {

	FILL_WORD_CREATE("填词创作", QuestionType.FILL_WORD_CREATING, 60),

	SPEED("速度", QuestionType.FILL_WORD_CREATING, 10),

	STRONG_WEAK("强弱", QuestionType.FILL_WORD_CREATING, 10),

	PULSE("律动", QuestionType.FILL_WORD_CREATING, 20),

	POSTURE("体态", QuestionType.ART_ACT, 20),

	COOPERATION("协作", QuestionType.ART_ACT, 20),

	SENTIMENT("情绪", QuestionType.ART_ACT, 30),

	PULSE2("律动", QuestionType.ART_ACT, 30),

	SPEED2("速度", QuestionType.RHYTHM_CREATING, 5),

	STRONG_WEAK2("强弱", QuestionType.RHYTHM_CREATING, 15),

	TEMPO("节拍", QuestionType.RHYTHM_CREATING, 5),

	RHYTHM("节奏", QuestionType.RHYTHM_CREATING, 40),

	TIMBRE("音色", QuestionType.RHYTHM_CREATING, 15),

	RATION_MUSICAL_INSTRUMENT("配器", QuestionType.RHYTHM_CREATING, 20),

	MELODY("旋律", QuestionType.MELODY_CREATING, 40),

	TABLATURE("记谱法", QuestionType.MELODY_CREATING, 20),

	TEMPO2("节拍", QuestionType.MELODY_CREATING, 5),

	RHYTHM2("节奏", QuestionType.MELODY_CREATING, 25),

	SPEED3("速度", QuestionType.MELODY_CREATING, 5),

	STRONG_WEAK3("强弱", QuestionType.MELODY_CREATING, 5),

	MELODY2("旋律", QuestionType.SONG_CREATING, 20),

	TABLATURE2("记谱法", QuestionType.SONG_CREATING, 10),

	SONG_WORD("歌词", QuestionType.SONG_CREATING, 20),

	TEMPO3("节拍", QuestionType.SONG_CREATING, 5),

	RHYTHM3("节奏", QuestionType.SONG_CREATING, 10),

	SPEED4("速度", QuestionType.SONG_CREATING, 5),

	TIMBRE2("音色", QuestionType.SONG_CREATING, 10),

	RATION_MUSICAL_INSTRUMENT2("配器", QuestionType.SONG_CREATING, 20),

	MELODY3("旋律", QuestionType.ASSIGN_MUSIC_CREATING, 15),

	RHYTHM4("节奏", QuestionType.ASSIGN_MUSIC_CREATING, 10),

	MUSICAL_FORM("曲式", QuestionType.ASSIGN_MUSIC_CREATING, 10),

	HARMONY("和声", QuestionType.ASSIGN_MUSIC_CREATING, 25),

	TEXTURE("织体", QuestionType.ASSIGN_MUSIC_CREATING, 20),

	RATION_MUSICAL_INSTRUMENT3("配器", QuestionType.ASSIGN_MUSIC_CREATING, 20);

	public enum Level {

		A("（95-100）"), A_("（90-95）"), B("（85-90）"), B_("（80-85）"), C("（75-80）"), C_("（70-75）"), D("（65-70）"), D_(
				"（60-65）"), E("（0-60）");

		private Level(String interval) {
			this.interval = interval;
		}

		private String interval;

		public static Level getLevle(Integer score) {
			if (null == score) {
				return E;
			}
			if (score >= 95) {
				return A;
			} else if (score >= 90) {
				return A_;
			} else if (score >= 85) {
				return B;
			} else if (score >= 80) {
				return B_;
			} else if (score >= 75) {
				return C;
			} else if (score >= 70) {
				return C_;
			} else if (score >= 65) {
				return D;
			} else if (score >= 60) {
				return D_;
			}
			return E;
		}

		public String getInterval() {
			return interval;
		}

		public void setInterval(String interval) {
			this.interval = interval;
		}

	}

	private Maluation(String title, QuestionType questionType, Integer score) {
		this.title = title;
		this.score = score;
		this.questionType = questionType;
	}

	private String title;

	private Integer score;

	private QuestionType questionType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

}
