package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 弱起特征
 */
@Entity
@DiscriminatorValue("ruo_qi")
public class RuoQiImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -399443054595066952L;

	private Boolean hasRuoQi;

	public Boolean getHasRuoQi() {
		return hasRuoQi;
	}

	public void setHasRuoQi(Boolean hasRuoQi) {
		this.hasRuoQi = hasRuoQi;
	}

}
