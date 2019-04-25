package com.nercl.music.cloud.entity;

public enum MaluationOption {

	FILL_WORD_CREATE_LEVEL1(Maluation.FILL_WORD_CREATE, "能按要求为所学歌曲创填歌词，但声韵和节奏律动混乱（0-18）", 0, 18),

	FILL_WORD_CREATE_LEVEL2(Maluation.FILL_WORD_CREATE, "能按要求为所学歌曲创填歌词，声韵和节奏律动一般（19-47）", 19, 47),

	FILL_WORD_CREATE_LEVEL3(Maluation.FILL_WORD_CREATE, "能按要求为所学歌曲创填歌词，兼顾声韵和节奏律动，歌词内容极具创意（48-60）", 48, 60),

	SPEED_LEVEL1(Maluation.SPEED, "没有速度概念（0-3）", 0, 3),

	SPEED_LEVEL2(Maluation.SPEED, "速度概念不敏感（4-7）", 4, 7),

	SPEED_LEVEL3(Maluation.SPEED, "速度概念很敏感（8-10）", 8, 10),

	STRONG_WEAK_LEVEL1(Maluation.STRONG_WEAK, "填词强弱概念混乱，无法进行填词编创（0-3）", 0, 3),

	STRONG_WEAK_LEVEL2(Maluation.STRONG_WEAK, "有一定填词强弱规律编创概念，能准确应用（4-7）", 4, 7),

	STRONG_WEAK_LEVEL3(Maluation.STRONG_WEAK, "有很好的填词强弱规律概念，编创有创意（8-10）", 8, 10),

	PULSE_LEVEL1(Maluation.PULSE, "跟随音乐填词律动能力差，融入度弱（0-6）", 0, 6),

	PULSE_LEVEL2(Maluation.PULSE, "基本能跟随音乐进行填词律动，表达应用略显生硬（7-15）", 7, 15),

	PULSE_LEVEL3(Maluation.PULSE, "能根据旋律的起伏找到填词律动规律，自如的表达个性化创作意图（16-20）", 16, 20),
	
	
	POSTURE_LEVEL1(Maluation.POSTURE, "表演时体态较紧张，动作经常出错，综合表现力差（0-6）", 0, 6),

	POSTURE_LEVEL2(Maluation.POSTURE, "表演时体态流畅度略弱，偶尔动作出错，能基本完成综合表演（7-15）", 7, 15),

	POSTURE_LEVEL3(Maluation.POSTURE, "表演时体态自然，节奏感好，动作规范，综合表现力好（16-20）", 16, 20),
	
	COOPERATION_LEVEL1(Maluation.COOPERATION, "表演中协作能力差，不能很好的融入集体综合表演（0-6）", 0, 6),

	COOPERATION_LEVEL2(Maluation.COOPERATION, "表演中能按要求跟随配合其他同学，有一定的参与协调能力（7-15）", 7, 15),

	COOPERATION_LEVEL3(Maluation.COOPERATION, "表演中组织协调能力强，极具凝聚力和创造力（16-20）", 16, 20),
	
	SENTIMENT_LEVEL1(Maluation.SENTIMENT, "不愿意参与演出（0-9）", 0, 9),

	SENTIMENT_LEVEL2(Maluation.SENTIMENT, "能基本正确的表达出演出作品的情绪与情感（10-23）", 10, 23),

	SENTIMENT_LEVEL3(Maluation.SENTIMENT, "能准确、生动活泼的表达出演出作品的情绪与情感（24-30）", 24, 30),
	
	PULSE2_LEVEL1(Maluation.PULSE2, "表演过程中把握作品律动能力差，肢体动作失误较多（0-9）", 0, 9),

	PULSE2_LEVEL2(Maluation.PULSE2, "表演过程中大致能把握作品律动，肢体动作偶有失误、有一定的的艺术感染力（10-23）", 10, 23),

	PULSE2_LEVEL3(Maluation.PULSE2, "表演过程中能准确把握作品律动，肢体配合生动、有较强的艺术感染力（24-30）", 24, 30),
	
	
	MELODY2_LEVEL1(Maluation.MELODY2, "没有掌握歌曲旋律编创的基础知识，没有段落、主题组织概念，无法创作歌曲旋律（0-6）", 0, 6),

	MELODY2_LEVEL2(Maluation.MELODY2, "基本掌握旋律编创的组织形式，旋律结构基本完整（7-15）", 7, 15),

	MELODY2_LEVEL3(Maluation.MELODY2, "熟练掌握歌曲旋律编创的组织形式，主题明晰，结构严谨，同时具有极好的个人创作特点（16-20）", 16, 20),
	
	TABLATURE2_LEVEL1(Maluation.TABLATURE2, "基本没有掌握记谱法知识，无法进行歌曲编创（0-3）", 0, 3),

	TABLATURE2_LEVEL2(Maluation.TABLATURE2, "基本掌握记谱法知识，个别记谱规则应用出错（4-7）", 4, 7),

	TABLATURE2_LEVEL3(Maluation.TABLATURE2, "熟练掌握记谱法知识，能流畅的应用至歌曲创作中（8-10）", 8, 10),
	
	SONG_WORD_LEVEL1(Maluation.SONG_WORD, "歌词结构段落混乱、没有字词节韵，基本无法进行歌词编创（0-6）", 0, 6),

	SONG_WORD_LEVEL2(Maluation.SONG_WORD, "歌词结构工整，段落清晰，节韵有所欠缺，创作应用略生硬 （7-15）", 7, 15),

	SONG_WORD_LEVEL3(Maluation.SONG_WORD, "歌词结构清晰，形象鲜明，朗朗上口，有节有韵，段落分明，能配合旋律充分的表达个性化创作意图（16-20）", 16, 20),
	
	TEMPO3_LEVEL1(Maluation.TEMPO3, "没有掌握节拍概念，不能进行编创应用（0-1）", 0, 1),

	TEMPO3_LEVEL2(Maluation.TEMPO3, "基本掌握节拍概念，能满足基本的创作要求（2-3）", 2, 3),

	TEMPO3_LEVEL3(Maluation.TEMPO3, "熟练掌握节拍概念，能灵活应用表达创作需求（4-5）", 4, 5),
	
	RHYTHM3_LEVEL1(Maluation.RHYTHM3, "没有节奏型概念，基本无法进行编创活动（0-3）", 0, 3),

	RHYTHM3_LEVEL2(Maluation.RHYTHM3, "基本掌握一些基础节奏型，能完成歌曲创作需求，但个性化特点不够突出，还有提升空间（4-7）", 4, 7),

	RHYTHM3_LEVEL3(Maluation.RHYTHM3, "熟练掌握多种节奏型，能在歌曲创作中随机组合，充分的表达个性化创作意图，具有鲜明的个性特点（8-10）", 8, 10),
	
	SPEED4_LEVEL1(Maluation.SPEED4, "速度概念混乱，速度与歌曲创作主题不匹配（0-1）", 0, 1),

	SPEED4_LEVEL2(Maluation.SPEED4, "能为歌曲创作设置适合的表达速度，与作品整体融入度好（2-3）", 2, 3),

	SPEED4_LEVEL3(Maluation.SPEED4, "能在歌曲创作中灵活应用速度变化改变、烘托、塑造主题音乐形象，个性化创意极佳（4-5）", 4, 5),
	
	TIMBRE2_LEVEL1(Maluation.TIMBRE2, "音色编创想象力弱，所选音色与作品匹配度差，没有音色创意体现（0-3）", 0, 3),

	TIMBRE2_LEVEL2(Maluation.TIMBRE2, "具有一定的音色编创想象力，所选音色与作品整体融入度良好，个性化程度一般（4-7）", 4, 7),

	TIMBRE2_LEVEL3(Maluation.TIMBRE2, "具有丰富的音色编创想象力，所选音色能与歌曲创作高度融合，个性化创意极佳（8-10）", 8, 10),
	
	RATION_MUSICAL_INSTRUMENT2_LEVEL1(Maluation.RATION_MUSICAL_INSTRUMENT2, "不熟悉基础乐器的演奏技法，无法进行配器编创（0-6）", 0, 6),

	RATION_MUSICAL_INSTRUMENT2_LEVEL2(Maluation.RATION_MUSICAL_INSTRUMENT2, "基本掌握常见乐器的演奏技法，配器规则应用正确严谨，但个性化创意不足（7-15）", 7, 15),

	RATION_MUSICAL_INSTRUMENT2_LEVEL3(Maluation.RATION_MUSICAL_INSTRUMENT2, "熟练掌握多种乐器的演奏技法，能进行灵活的应用组合，显示出极具个性的配器创意（16-20）", 16, 20),
	
	
	MELODY3_LEVEL1(Maluation.MELODY3, "没有掌握旋律编创的基础知识，没有乐句、乐段组织概念，无法创作旋律（0-4）", 0, 4),

	MELODY3_LEVEL2(Maluation.MELODY3, "基本掌握旋律编创的组织形式，乐段清晰，乐句工整，旋律结构基本完整（5-11）", 5, 11),

	MELODY3_LEVEL3(Maluation.MELODY3, "熟练掌握旋律编创的组织形式，主题明晰，结构完整，同时具有极好的个人创作特点（12-15）", 12, 15),
	
	RHYTHM4_LEVEL1(Maluation.RHYTHM4, "没有节奏型概念，基本无法进行编创活动（0-3）", 0, 3),

	RHYTHM4_LEVEL2(Maluation.RHYTHM4, "基本掌握一些基础节奏型，能完成创作需求，但个性化特点不够突出，还有提升空间（4-7）", 4, 7),

	RHYTHM4_LEVEL3(Maluation.RHYTHM4, "熟练掌握多种节奏型，能在创作中随机组合，充分的表达个性化创作意图，具有鲜明的个性特点（8-10）", 8, 10),
	
	MUSICAL_FORM_LEVEL1(Maluation.MUSICAL_FORM, "乐曲的结构创作概念混乱， 基本无法创作（0-3）", 0, 3),

	MUSICAL_FORM_LEVEL2(Maluation.MUSICAL_FORM, "基本掌握乐曲的结构创作规律，曲调段落创作关联性和渗透性较弱，曲式创作应用略显生硬（4-7）", 4, 7),

	MUSICAL_FORM_LEVEL3(Maluation.MUSICAL_FORM, "熟练掌握乐曲的结构创作规律，曲调段落创作充分体现了曲式结构中“对比、展开、变奏、重复”四大原则，段落之间相互关联，互相渗透，同时能在命题音乐创作中充分体现出个性化创作特点（8-10）", 8, 10),
	
	HARMONY_LEVEL1(Maluation.HARMONY, "没有掌握和声编配原则，无法实现各声部相互协调（0-7）", 0, 7),

	HARMONY_LEVEL2(Maluation.HARMONY, "基本掌握和声编配原则，在统一的和声基础上，实现各声部相互组合成为协调的整体（8-19）", 8, 19),

	HARMONY_LEVEL3(Maluation.HARMONY, "熟练掌握和声编配原则，能为旋律构建和声色彩、结构以及配合其他因素，塑造音乐形象、表现音乐内容（20-25）", 20, 25),
	
	TEXTURE_LEVEL1(Maluation.TEXTURE, "没有掌握伴奏织体编配原则，无法实现伴奏织体编创（0-6）", 0, 6),

	TEXTURE_LEVEL2(Maluation.TEXTURE, "基本掌握伴奏织体编配原则，织体的旋律立体线条略微单调，多声部创作思维略弱（7-15）", 7, 15),

	TEXTURE_LEVEL3(Maluation.TEXTURE, "熟练掌握伴奏织体变化规律，能为旋律构建交错叠置的立体线条，塑造丰富、立体的多声部音乐形象、自如表达音乐创作灵感（16-20）", 16, 20),
	
	RATION_MUSICAL_INSTRUMENT3_LEVEL1(Maluation.RATION_MUSICAL_INSTRUMENT3, "不熟悉基础乐器的演奏技法和音色特点，配器编创中基本没有创意体现（0-6）", 0, 6),

	RATION_MUSICAL_INSTRUMENT3_LEVEL2(Maluation.RATION_MUSICAL_INSTRUMENT3, "基本掌握基础乐器的演奏技法、音色特点，具有一定的配器想象力，配器编创融入度良好，应用正确严谨，但个性化创意不足（7-15）", 7, 15),

	RATION_MUSICAL_INSTRUMENT3_LEVEL3(Maluation.RATION_MUSICAL_INSTRUMENT3, "熟练掌握多种乐器的演奏技法，具有丰富的音色想象力，配器能与旋律创作高度融合，显示出极具个性的配器组合创意（16-20）", 16, 20),
	

	MELODY_LEVEL1(Maluation.MELODY, "没有掌握旋律编创的基础知识，没有段落、主题组织概念，无法创作旋律（0-12）", 0, 12),

	MELODY_LEVEL2(Maluation.MELODY, "基本掌握旋律编创的组织形式，旋律结构基本完整（13-31）", 13, 31),

	MELODY_LEVEL3(Maluation.MELODY, "熟练掌握旋律编创的组织形式，主题明晰，结构完整，同时具有极好的个人创作特点（32-40）", 32, 40),
	
	TABLATURE_LEVEL1(Maluation.TABLATURE, "基本没有掌握记谱法知识，无法进行编创活动（0-6）", 0, 6),

	TABLATURE_LEVEL2(Maluation.TABLATURE, "基本掌握记谱法知识，个别记谱规则应用出错（7-15）", 7, 15),

	TABLATURE_LEVEL3(Maluation.TABLATURE, "熟练掌握记谱法知识，能流畅的应用至旋律创作中（16-20）", 16, 20),
	
	TEMPO2_LEVEL1(Maluation.TEMPO2, "没有掌握节拍概念，不能进行编创应用（0-1）", 0, 1),

	TEMPO2_LEVEL2(Maluation.TEMPO2, "基本掌握节拍概念，能满足基本的创作要求（2-3）", 2, 3),

	TEMPO2_LEVEL3(Maluation.TEMPO2, "熟练掌握节拍概念，能灵活应用表达创作需求（4-5）", 4, 5),
	
	RHYTHM2_LEVEL1(Maluation.RHYTHM2, "没有节奏型概念，基本无法进行编创活动（0-7）", 0, 7),

	RHYTHM2_LEVEL2(Maluation.RHYTHM2, "基本掌握一些基础节奏型，能完成创作需求，但个性化特点不够突出，还有提升空间（8-19）", 8, 19),

	RHYTHM2_LEVEL3(Maluation.RHYTHM2, "熟练掌握多种节奏型，能在创作中随机组合，充分的表达个性化创作意图，具有鲜明的个性特点（20-25）", 20, 25),
	
	SPEED3_LEVEL1(Maluation.SPEED3, "速度与旋律匹配概念混乱，速度与旋律不匹配（0-1）", 0, 1),

	SPEED3_LEVEL2(Maluation.SPEED3, "为旋律创作设置的速度基本匹配，还有提升的空间（2-3）", 2, 3),

	SPEED3_LEVEL3(Maluation.SPEED3, "能为旋律创作设置匹配的速度（4-5）", 4, 5),
	
	STRONG_WEAK3_LEVEL1(Maluation.STRONG_WEAK3, "没有掌握节拍、节奏强弱规律，无法配合旋律进行编创应用（0-1）", 0, 1),

	STRONG_WEAK3_LEVEL2(Maluation.STRONG_WEAK3, "基本掌握节拍、节奏型强弱规律，能配合旋律的变化，创作应用略显生硬（2-3）", 2, 3),

	STRONG_WEAK3_LEVEL3(Maluation.STRONG_WEAK3, "熟练掌握节拍、节奏的强弱规律，能根据旋律的变化加以灵活应用，表达个性化创作意图（4-5）", 4, 5),


	SPEED2_LEVEL1(Maluation.SPEED2, "没有速度设置概念（0-1）", 0, 1),

	SPEED2_LEVEL2(Maluation.SPEED2, "对速度设置概念不敏感（2-3）", 2, 3),

	SPEED2_LEVEL3(Maluation.SPEED2, "对速度设置概念很敏感（4-5）", 4, 5),
	
	STRONG_WEAK2_LEVEL1(Maluation.STRONG_WEAK2, "节拍、节奏强弱概念混乱，基本无法完成编创（0-4）", 0, 4),

	STRONG_WEAK2_LEVEL2(Maluation.STRONG_WEAK2, "有一定的节拍、节奏强弱规律编创概念，能准确应用（5-11）", 5, 11),

	STRONG_WEAK2_LEVEL3(Maluation.STRONG_WEAK2, "有很好的强弱规律概念，同时能灵活运用强拍弱位、弱位强拍的创作技法，拓展强弱规律的创作边界，个性化特征鲜明（12-15）", 12, 15),
	
	TEMPO_LEVEL1(Maluation.TEMPO, "节拍概念模糊，基本无法完成编创（0-1）", 0, 1),

	TEMPO_LEVEL2(Maluation.TEMPO, "节拍概念基本正确，能进行基础的编创 （2-3）", 2, 3),

	TEMPO_LEVEL3(Maluation.TEMPO, "节拍概念清晰准确，能灵活融入创作意图（4-5）", 4, 5),
	
	RHYTHM_LEVEL1(Maluation.RHYTHM, "节奏型概念不清晰，不能完成基础的编创（0-12）", 0, 12),

	RHYTHM_LEVEL2(Maluation.RHYTHM, "基本掌握节奏型编创技巧，节奏型应用较为单一，创意通感程度一般（13-31）", 13, 31),

	RHYTHM_LEVEL3(Maluation.RHYTHM, "熟练掌握多种节奏型，节奏型应用丰富，创意通感程度高（32-40）", 32, 40),
	
	TIMBRE_LEVEL1(Maluation.TIMBRE, "音色想象力弱，编创中基本没有创意体现（0-4）", 0, 4),

	TIMBRE_LEVEL2(Maluation.TIMBRE, "具有一定的音色想象力，编创融入度良好（5-11）", 5, 11),

	TIMBRE_LEVEL3(Maluation.TIMBRE, "具有丰富的音色想象力，编创融入度高（12-15）", 12, 15),
	
	RATION_MUSICAL_INSTRUMENT_LEVEL1(Maluation.RATION_MUSICAL_INSTRUMENT, "不熟悉打击乐器的音色特点，无法在编创中应用展示（0-6）", 0, 6),

	RATION_MUSICAL_INSTRUMENT_LEVEL2(Maluation.RATION_MUSICAL_INSTRUMENT, "基本掌握打击乐器的音色特点，在编创中有一定的应用展示（7-15）", 7, 15),

	RATION_MUSICAL_INSTRUMENT_LEVEL3(Maluation.RATION_MUSICAL_INSTRUMENT, "熟悉打击乐器的音色特点，在编创中能加以灵活运用（16-20）", 16, 20);
	


	private MaluationOption(Maluation maluation, String title, Integer lowScore, Integer highScore) {
		this.maluation = maluation;
		this.title = title;
		this.lowScore = lowScore;
		this.highScore = highScore;
	}

	private String title;

	private Integer lowScore;

	private Integer highScore;

	private Maluation maluation;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLowScore() {
		return lowScore;
	}

	public void setLowScore(Integer lowScore) {
		this.lowScore = lowScore;
	}

	public Integer getHighScore() {
		return highScore;
	}

	public void setHighScore(Integer highScore) {
		this.highScore = highScore;
	}

	public Maluation getMaluation() {
		return maluation;
	}

	public void setMaluation2(Maluation maluation) {
		this.maluation = maluation;
	}

}
