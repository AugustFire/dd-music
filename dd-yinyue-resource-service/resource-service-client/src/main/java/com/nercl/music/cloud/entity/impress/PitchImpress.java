package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 音高特征
 */
@Entity
@DiscriminatorValue("pitch")
public class PitchImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6574811457020209759L;

	private String pitch;

	public String getPitch() {
		return pitch;
	}

	public void setPitch(String pitch) {
		this.pitch = pitch;
	}

}
