package com.nercl.music.cloud.entity.classroom;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * 返回所有enum的值的list
	 */
	public static List<String> enumList() {
		List<String> list = new ArrayList<String>();
		for (VersionDesc v : VersionDesc.values()) {
			list.add(v.getDesc());
		}
		return list;
	}

	/**
	 * 根据描述返回对应的enum
	 */
	public static VersionDesc getVersionDescByDesc(String desc) {
		for (VersionDesc v : VersionDesc.values()) {
			if (v.getDesc().equals(desc)) {
				return v;
			}
		}
		return null;
	}

}
