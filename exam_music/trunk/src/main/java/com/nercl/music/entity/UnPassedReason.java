package com.nercl.music.entity;

public enum UnPassedReason {

	REASON1("个人资料不准确"), 

	REASON2("报名时间已过"),

	REASON3("审核失败原因3"),

	REASON4("审核失败原因4");

	private String reason;
	
	private UnPassedReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
	public static String get() {
		StringBuffer reason = new StringBuffer();
		for(UnPassedReason unPassedReason :UnPassedReason.values()){
			reason.append(unPassedReason.getReason());
			reason.append(";");
		}
		
		return reason.toString();
		
	}
}
