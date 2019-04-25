package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 节奏型特征
 */
@Entity
@DiscriminatorValue("rhythm")
public class RhythmImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -464878632872672135L;

	private String rhythm;

	public String getRhythm() {
		return rhythm;
	}

	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}

}
