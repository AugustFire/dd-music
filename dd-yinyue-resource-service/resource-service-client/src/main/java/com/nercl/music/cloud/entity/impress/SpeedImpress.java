package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 速度特征
 */
@Entity
@DiscriminatorValue("speed")
public class SpeedImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5117351293177662753L;

	private Integer speed;

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

}
