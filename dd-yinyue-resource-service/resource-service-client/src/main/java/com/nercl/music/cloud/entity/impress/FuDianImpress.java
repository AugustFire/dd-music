package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 附点特征
 */
@Entity
@DiscriminatorValue("fu_dian")
public class FuDianImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7939839625274175417L;

	private Boolean hasFuDian;

	public Boolean getHasFuDian() {
		return hasFuDian;
	}

	public void setHasFuDian(Boolean hasFuDian) {
		this.hasFuDian = hasFuDian;
	}

}
