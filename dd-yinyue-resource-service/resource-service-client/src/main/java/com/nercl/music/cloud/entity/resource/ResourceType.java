package com.nercl.music.cloud.entity.resource;

public enum ResourceType {

	/**
	 * 个人资源
	 */
	PERSONAL_RESOURCE("个人资源"),

	/**
	 * 节奏训练资源
	 */
	RHYTHM_RESOURCE("节奏训练资源"),

	/**
	 * 教本资源
	 */
	BOOK_RESOURCE("教本资源"),

	/**
	 * 专题资源
	 */
	TOPIC_RESOURCE("专题资源"),

	/**
	 * 题目资源
	 */
	QUESTION_RESOURCE("题目资源"),

	/**
	 * 歌曲资源
	 */
	SONG_RESOURCE("歌曲资源"),

	/**
	 * 普通资源
	 */
	RESOURCE("普通资源");

	private String desc;

	private ResourceType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return this.desc;
	}

	/**
	 * 根据描述返回对应的enum
	 */
	public static ResourceType getResourceTypeByDesc(String desc) {
		for (ResourceType r : ResourceType.values()) {
			if (r.getDesc().equals(desc)) {
				return r;
			}
		}
		return null;
	}
}
