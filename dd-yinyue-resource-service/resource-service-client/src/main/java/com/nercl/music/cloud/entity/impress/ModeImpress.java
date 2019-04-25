package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 调式，调性特征
 */
@Entity
@DiscriminatorValue("mode")
public class ModeImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -763894717564643608L;

	private String keySig;

	private String type;

	private String contentKeyNote;

	private String contentMode;

	public String getKeySig() {
		return keySig;
	}

	public void setKeySig(String keySig) {
		this.keySig = keySig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContentKeyNote() {
		return contentKeyNote;
	}

	public void setContentKeyNote(String contentKeyNote) {
		this.contentKeyNote = contentKeyNote;
	}

	public String getContentMode() {
		return contentMode;
	}

	public void setContentMode(String contentMode) {
		this.contentMode = contentMode;
	}

}
