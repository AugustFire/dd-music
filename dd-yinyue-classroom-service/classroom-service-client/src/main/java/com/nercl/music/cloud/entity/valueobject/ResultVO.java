package com.nercl.music.cloud.entity.valueobject;

import java.io.Serializable;

public class ResultVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5150711012167343926L;

	private String id;

	/**
	 * 年度
	 */
	private Integer year;

	/**
	 * 分值
	 */
	private Integer score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	@Override
	public String toString(){
		return "id:"+this.id+",score:"+this.score+",year:"+this.year;
	}

}
