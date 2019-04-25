package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 旋律线特征
 */
@Entity
@DiscriminatorValue("melody_line")
public class MelodyLineImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1379704241689732490L;

	private Boolean hasMelodyLine;

	public Boolean getHasMelodyLine() {
		return hasMelodyLine;
	}

	public void setHasMelodyLine(Boolean hasMelodyLine) {
		this.hasMelodyLine = hasMelodyLine;
	}

}
