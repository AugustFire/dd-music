package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 音程特征
 */
@Entity
@DiscriminatorValue("interval")
public class IntervalImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2269200816951703117L;

	private String intervals;

	private String notea;

	private String noteb;

	public String getIntervals() {
		return intervals;
	}

	public void setIntervals(String intervals) {
		this.intervals = intervals;
	}

	public String getNotea() {
		return notea;
	}

	public void setNotea(String notea) {
		this.notea = notea;
	}

	public String getNoteb() {
		return noteb;
	}

	public void setNoteb(String noteb) {
		this.noteb = noteb;
	}

}
