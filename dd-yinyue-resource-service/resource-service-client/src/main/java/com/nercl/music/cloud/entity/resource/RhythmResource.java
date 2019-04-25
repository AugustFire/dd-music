package com.nercl.music.cloud.entity.resource;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("rhythm")
public class RhythmResource extends Resource {

	/**
	 * 节奏训练资源
	 */
	private static final long serialVersionUID = -4423333776330971469L;

}
