package com.nercl.music.entity;

public enum UnPassReasonForQuestion {
	REASON1("审核失败原因1"), 

	REASON2("审核失败原因2"),

	REASON3("审核失败原因3"),

	REASON4("审核失败原因4");

	private String reason;
	
	private UnPassReasonForQuestion(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
	
	public static String get() {
		StringBuffer reason = new StringBuffer();
		for(UnPassReasonForQuestion unPassedReason :UnPassReasonForQuestion.values()){
			reason.append(unPassedReason.getReason());
			reason.append(";");
		}
		
		return reason.toString();
		
	}
}
