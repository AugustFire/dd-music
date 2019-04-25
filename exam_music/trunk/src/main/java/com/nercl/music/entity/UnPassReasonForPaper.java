package com.nercl.music.entity;

public enum UnPassReasonForPaper {
	REASON1("审核失败原因1"), 

	REASON2("审核失败原因2"),

	REASON3("审核失败原因3"),

	REASON4("审核失败原因4");

	private String reason;
	
	private UnPassReasonForPaper(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
	public static String get() {
		StringBuffer reason = new StringBuffer();
		for(UnPassReasonForPaper unPassedReason :UnPassReasonForPaper.values()){
			reason.append(unPassedReason.getReason());
			reason.append(";");
		}
		
		return reason.toString();
		
	}
}
