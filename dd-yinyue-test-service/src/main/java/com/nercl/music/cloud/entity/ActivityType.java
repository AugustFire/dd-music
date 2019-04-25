package com.nercl.music.cloud.entity;

/**
 * 艺术活动类型
 * <p>
 * 老师-演出、比赛、公开课
 * <p>
 * 学生-音乐考级、比赛获奖
 * </p>
 */
public enum ActivityType {

	/**
	 * 老师-演出
	 */
	TEACHER_PERFORMANCE,

	/**
	 * 老师-比赛
	 */
	TEACHER_COMPETITION,

	/**
	 * 老师-公开课
	 */
	TEACHER_PUBLIC_CLASS,

	/**
	 * 学生-音乐考级
	 */
	STUDENT_GRADE_EXAMINATION,
	/**
	 * 学生-比赛获奖
	 */
	STUDENT_COMPETITION_AWARD;

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		ActivityType[] en = ActivityType.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}
}
