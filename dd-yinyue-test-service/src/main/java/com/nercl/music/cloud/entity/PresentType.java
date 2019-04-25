package com.nercl.music.cloud.entity;

public enum PresentType {

	NUMBERED("点点画儿歌"),

	DRUM("smart打击乐"),

	STAFF("smart五线谱");

	private String desc;

	private PresentType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
