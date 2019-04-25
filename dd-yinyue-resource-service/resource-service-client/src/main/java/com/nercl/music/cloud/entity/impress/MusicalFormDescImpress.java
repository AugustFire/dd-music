package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 曲式描述特征
 */
@Entity
@DiscriminatorValue("musical_form_desc")
public class MusicalFormDescImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5916312091278280302L;

	private String musicalFormDesc;

	public String getMusicalFormDesc() {
		return musicalFormDesc;
	}

	public void setMusicalFormDesc(String musicalFormDesc) {
		this.musicalFormDesc = musicalFormDesc;
	}

}
