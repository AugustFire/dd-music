package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 节拍特征
 */
@Entity
@DiscriminatorValue("beat")
public class BeatImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5866394242567249621L;

	private String beat;

	public String getBeat() {
		return beat;
	}

	public void setBeat(String beat) {
		this.beat = beat;
	}

}
