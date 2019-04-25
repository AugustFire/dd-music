package com.nercl.music.cloud.entity.impress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 曲式特征
 */
@Entity
@DiscriminatorValue("musical_form")
public class MusicalFormImpress extends Impress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1803085564605141925L;

	private String text;

	private Integer startBar;

	private Integer endBar;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getStartBar() {
		return startBar;
	}

	public void setStartBar(Integer startBar) {
		this.startBar = startBar;
	}

	public Integer getEndBar() {
		return endBar;
	}

	public void setEndBar(Integer endBar) {
		this.endBar = endBar;
	}

}
