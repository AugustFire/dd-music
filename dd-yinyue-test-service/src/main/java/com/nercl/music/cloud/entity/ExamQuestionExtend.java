package com.nercl.music.cloud.entity;

import java.util.List;

/**
 * ExanQuestion的拓展类
 * */
public class ExamQuestionExtend extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5362259341614377930L;

	/**
	 * 答案
	 * */
	private List<Answer> listAnswer;
	
	/**
	 * 选项
	 * */
	private List<Option> listOption;

	public List<Answer> getListAnswer() {
		return listAnswer;
	}

	public void setListAnswer(List<Answer> listAnswer) {
		this.listAnswer = listAnswer;
	}

	public List<Option> getListOption() {
		return listOption;
	}

	public void setListOption(List<Option> listOption) {
		this.listOption = listOption;
	}
	
}
