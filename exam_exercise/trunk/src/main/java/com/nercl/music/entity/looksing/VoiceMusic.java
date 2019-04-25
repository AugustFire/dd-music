package com.nercl.music.entity.looksing;

public class VoiceMusic {

	private int id;

	private int key;

	private int sequenceNo;

	private long absoluteTime;

	private int noteNumber;

	private String noteName;

	private int rectifyNote;

	private String rectifyName;

	private int pitchToFreq;

	private int noteLength;

	private String noteIsTrue;

	private String lengthIstrue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public long getAbsoluteTime() {
		return absoluteTime;
	}

	public void setAbsoluteTime(long absoluteTime) {
		this.absoluteTime = absoluteTime;
	}

	public int getNoteNumber() {
		return noteNumber;
	}

	public void setNoteNumber(int noteNumber) {
		this.noteNumber = noteNumber;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public int getRectifyNote() {
		return rectifyNote;
	}

	public void setRectifyNote(int rectifyNote) {
		this.rectifyNote = rectifyNote;
	}

	public int getPitchToFreq() {
		return pitchToFreq;
	}

	public void setPitchToFreq(int pitchToFreq) {
		this.pitchToFreq = pitchToFreq;
	}

	public int getNoteLength() {
		return noteLength;
	}

	public void setNoteLength(int noteLength) {
		this.noteLength = noteLength;
	}

	public String getNoteIsTrue() {
		return noteIsTrue;
	}

	public void setNoteIsTrue(String noteIsTrue) {
		this.noteIsTrue = noteIsTrue;
	}

	public String getLengthIstrue() {
		return lengthIstrue;
	}

	public void setLengthIstrue(String lengthIstrue) {
		this.lengthIstrue = lengthIstrue;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getRectifyName() {
		return rectifyName;
	}

	public void setRectifyName(String rectifyName) {
		this.rectifyName = rectifyName;
	}
}
