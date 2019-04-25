package com.nercl.music.cloud.entity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

public enum CompositeAbility {

	/**
	 * 识谱
	 */
	STAFF("YYNL1-1", "识谱", null),

	/**
	 * 认识乐谱
	 */
	READ_STAFF("YYNL1-1-1", "认识乐谱", STAFF),

	/**
	 * 分析乐谱
	 */
	ANALYZE_STAFF("YYNL1-1-2", "分析乐谱", STAFF),

	/**
	 * 运用乐谱
	 */
	USED_STAFF("YYNL1-1-3", "运用乐谱", STAFF),

	/**
	 * 视唱
	 */
	SIGHT_SINGING("YYNL1-2", "视唱", null),

	/**
	 * 单音视唱
	 */
	SINGLE_NOTES_SIGHT_SINGING("YYNL1-2-1", "单音视唱", SIGHT_SINGING),

	/**
	 * 音程视唱
	 */
	INTERVAL_SIGHT_SINGING("YYNL1-2-2", "音程视唱", SIGHT_SINGING),

	/**
	 * 短句视唱
	 */
	CLAUSE_SIGHT_SINGING("YYNL1-2-3", "短句视唱", SIGHT_SINGING),

	/**
	 * 练耳
	 */
	EAR_TRAINING("YYNL1-3", "练耳", null),

	/**
	 * 听辨
	 */
	LISTEN_DECIDE_EAR_TRAINING("YYNL1-3-1", "听辨", EAR_TRAINING),

	/**
	 * 听写
	 */
	LISTEN_WRITE_EAR_TRAINING("YYNL1-3-2", "听写", EAR_TRAINING),

	/**
	 * 编创
	 */
	CREATING("YYNL1-4", "编创", null),

	/**
	 * 填词编创
	 */
	FILL_WORD_CREATING("YYNL1-4-1", "填词编创", CREATING),

	/**
	 * 即兴编创
	 */
	IMPROVISATION_CREATING("YYNL1-4-2", "即兴编创", CREATING),

	/**
	 * 创作实践
	 */
	PRACTICE_CREATING("YYNL1-4-3", "创作实践", CREATING),

	/**
	 * 音乐表演
	 */
	MUSIC_PERFORMANCE("YYNL1-5", "音乐表演", null),

	/**
	 * 演唱
	 */
	SING_MUSIC_PERFORMANCE("YYNL1-5-1", "演唱", MUSIC_PERFORMANCE),

	/**
	 * 演奏
	 */
	PLAY_MUSIC_PERFORMANCE("YYNL1-5-2", "演奏", MUSIC_PERFORMANCE),

	/**
	 * 背唱
	 */
	RECITE_SING_MUSIC_PERFORMANCE("YYNL1-5-3", "背唱", MUSIC_PERFORMANCE),

	/**
	 * 背奏
	 */
	RECITE_PLAY_MUSIC_PERFORMANCE("YYNL1-5-4", "背奏", MUSIC_PERFORMANCE),

	/**
	 * 音乐鉴赏
	 */
	MUSIC_APPRECIATE("YYNL1-6", "音乐鉴赏", null),

	/**
	 * 聆听感受
	 */
	LISTEN_FEELING_MUSIC_APPRECIATE("YYNL1-6-1", "聆听感受", MUSIC_APPRECIATE),

	/**
	 * 评价鉴赏
	 */
	EVALUATE_APPRECIATE_MUSIC_APPRECIATE("YYNL1-6-2", "评价鉴赏", MUSIC_APPRECIATE),

	/**
	 * 综合艺术表演
	 */
	COMPOSITE_ART_PERFORMANCE("YYNL1-7", "综合艺术表演", null),

	/**
	 * 舞蹈
	 */
	DANCE_COMPOSITE_ART_PERFORMANCE("YYNL1-7-1", "舞蹈", COMPOSITE_ART_PERFORMANCE),

	/**
	 * 律动
	 */
	PULSE_COMPOSITE_ART_PERFORMANCE("YYNL1-7-2", "律动", COMPOSITE_ART_PERFORMANCE),

	/**
	 * 音乐游戏
	 */
	MUSIC_GAME_COMPOSITE_ART_PERFORMANCE("YYNL1-7-3", "音乐游戏", COMPOSITE_ART_PERFORMANCE);

	private String code;

	private String title;

	private CompositeAbility parent;

	private CompositeAbility(String code, String title, CompositeAbility parent) {
		this.code = code;
		this.title = title;
		this.parent = parent;
	}

	public CompositeAbility getParent() {
		return parent;
	}

	public void setParent(CompositeAbility parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * <p>
	 * 判断一个以逗号分隔的字符串是否是由本枚举中的值组成的，全部都是则返回true，否则返回false
	 * </p>
	 */
	public static Boolean isInCompositeAbility(String str) {
		if (Strings.isNullOrEmpty(str)) {
			return false;
		}
		List<String> eunm = Arrays.asList(str.split(","));
		List<String> compositeAbilityName = Arrays.stream(CompositeAbility.values()).map(CompositeAbility::name)
				.collect(Collectors.toList());
		return compositeAbilityName.containsAll(eunm);
	}

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static Boolean isDefined(String str) {
		return Arrays.stream(CompositeAbility.values()).anyMatch(ca -> String.valueOf(ca).equals(str));
	}

	public static List<CompositeAbility> getChildren(CompositeAbility parent) {
		return Arrays.stream(CompositeAbility.values()).filter(ca -> parent.equals(ca.getParent()))
				.collect(Collectors.toList());
	}
}
