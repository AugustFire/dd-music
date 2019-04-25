package com.nercl.music.entity;

public class SourceMusic {
    private int sequenceNo;
	private int absoluteTime;
	private int noteNumber;
	private String noteName;
	private int pitchToFreq;
	private int virtualFreq;
	private int  noteLength;
	public long getAbsoluteTime() {
		return absoluteTime;
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
	
	public int getPitchToFreq() {
		return pitchToFreq;
	}
	public void setPitchToFreq(int pitchToFreq) {
		this.pitchToFreq = pitchToFreq;
	}
	public int getVirtualFreq() {
		return virtualFreq;
	}
	public void setVirtualFreq(int virtualFreq) {
		this.virtualFreq = virtualFreq;
	}
	public int getNoteLength() {
		return noteLength;
	}
	public void setNoteLength(int noteLength) {
		this.noteLength = noteLength;
	}
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public void setAbsoluteTime(int absoluteTime) {
		this.absoluteTime = absoluteTime;
	}
}
