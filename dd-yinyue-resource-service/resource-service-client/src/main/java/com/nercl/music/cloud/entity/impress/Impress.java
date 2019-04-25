package com.nercl.music.cloud.entity.impress;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nercl.music.cloud.entity.resource.Dimension;
import com.nercl.music.cloud.entity.song.Song;

@Entity
@Table(name = "impresses", indexes = { @Index(columnList = "dimension", unique = false) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("impress")
public class Impress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4492329089937052394L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/**
	 * 维度
	 */
	@Enumerated(EnumType.STRING)
	private Dimension dimension;

	/**
	 * 领域
	 */
	@Enumerated(EnumType.STRING)
	private Field field;

	/**
	 * 歌曲
	 */
	@Column(name = "song_id")
	private String songId;

	/**
	 * 歌曲
	 */
	@ManyToOne
	@JoinColumn(name = "song_id", insertable = false, updatable = false)
	private Song song;

	public enum Field {

		/**
		 * 中小学
		 */
		MIDDLE_PRIMARY_SCHOOL,

		/**
		 * 大学
		 */
		UNIVERSITY,

		/**
		 * 培训机构
		 */
		TRAINING_ORGANIZATION;

	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getId() {
		return id;
	}

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

}
