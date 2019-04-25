package com.nercl.music.cloud.entity.resource;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 教本资源
 */
@Entity
@DiscriminatorValue("song")
public class SongResource extends Resource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8227069145614194160L;

}
