package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 音域特征
 */
@Entity
@DiscriminatorValue("volume_range")
public class VolumeRangeImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8755137524279571433L;

	private String volumeRange;

	public String getVolumeRange() {
		return volumeRange;
	}

	public void setVolumeRange(String volumeRange) {
		this.volumeRange = volumeRange;
	}

}
