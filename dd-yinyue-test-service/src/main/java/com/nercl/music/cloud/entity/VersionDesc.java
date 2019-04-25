package com.nercl.music.cloud.entity;

public enum VersionDesc {

	REN_JIAO_BAN("人教版"),

	REN_YIN_BAN("人音版"),

	XIANG_JIAO_BAN("湘教版"),

	SU_JIAO_BAN("苏教版");

	private String desc;

	private VersionDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
