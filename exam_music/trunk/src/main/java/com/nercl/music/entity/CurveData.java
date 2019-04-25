package com.nercl.music.entity;

import java.util.List;

public class CurveData {
	public List<Double> voicePitchs;
	public class VoiceNote{
		public int absoluteTime;
		public int noteNumber;
		public String noteName;
		public int noteLength;
		public int getAbsoluteTime() {
			return absoluteTime;
		}
		public void setAbsoluteTime(int absoluteTime) {
			this.absoluteTime = absoluteTime;
		}
		public int getNoteNumber() {
			return noteNumber;
		}
		public void setNoteNumber(int noteNumber) {
			this.noteNumber = noteNumber;
		}
		public int getNoteLength() {
			return noteLength;
		}
		public void setNoteLength(int noteLength) {
			this.noteLength = noteLength;
		}
		public String getNoteName() {
			return noteName;
		}
		public void setNoteName(String noteName) {
			this.noteName = noteName;
		}	
	}
	public List<VoiceNote> voiceNotes;
	public int scale;
	public List<Double> getVoicePitchs() {
		return voicePitchs;
	}
	public void setVoicePitchs(List<Double> voicePitchs) {
		this.voicePitchs = voicePitchs;
	}
	public List<VoiceNote> getVoiceNotes() {
		return voiceNotes;
	}
	public void setVoiceNotes(List<VoiceNote> voiceNotes) {
		this.voiceNotes = voiceNotes;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
}
