package com.nercl.music.entity.looksing;

import java.util.List;

public class MusicData {

	private List<SourceMusic> sourceMusicList;

	private List<VoiceMusic> voiceMusicList;

	private List<SourceAndVoiceMusic> sourceAndVoiceMusicList;

	private int noteCount;

	private int sampleSpeed;

	private int voiceSpeed;

	private int noteNo;

	private int lengthNo;

	private double trueNO;

	private double accuracy;

	private long maxTime;

	public List<SourceMusic> getSourceMusicList() {
		return sourceMusicList;
	}

	public void setSourceMusicList(List<SourceMusic> sourceMusicList) {
		this.sourceMusicList = sourceMusicList;
	}

	public List<VoiceMusic> getVoiceMusicList() {
		return voiceMusicList;
	}

	public void setVoiceMusicList(List<VoiceMusic> voiceMusicList) {
		this.voiceMusicList = voiceMusicList;
	}

	public int getNoteCount() {
		return noteCount;
	}

	public void setNoteCount(int noteCount) {
		this.noteCount = noteCount;
	}

	public int getSampleSpeed() {
		return sampleSpeed;
	}

	public void setSampleSpeed(int sampleSpeed) {
		this.sampleSpeed = sampleSpeed;
	}

	public int getVoiceSpeed() {
		return voiceSpeed;
	}

	public void setVoiceSpeed(int voiceSpeed) {
		this.voiceSpeed = voiceSpeed;
	}

	public int getNoteNo() {
		return noteNo;
	}

	public void setNoteNo(int noteNo) {
		this.noteNo = noteNo;
	}

	public int getLengthNo() {
		return lengthNo;
	}

	public void setLengthNo(int lengthNo) {
		this.lengthNo = lengthNo;
	}

	public double getTrueNO() {
		return trueNO;
	}

	public void setTrueNO(double trueNO) {
		this.trueNO = trueNO;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public List<SourceAndVoiceMusic> getSourceAndVoiceMusicList() {
		return sourceAndVoiceMusicList;
	}

	public void setSourceAndVoiceMusicList(List<SourceAndVoiceMusic> sourceAndVoiceMusicList) {
		this.sourceAndVoiceMusicList = sourceAndVoiceMusicList;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}
}
