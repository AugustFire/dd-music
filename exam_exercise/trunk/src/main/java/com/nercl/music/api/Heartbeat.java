package com.nercl.music.api;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat extends Observable {

	private static final Integer VALID_TIME = 45 * 1000;

	private String exerciseId;

	private long createAt;

	private Timer timer;

	public Heartbeat(String exerciseId, Observer observer) {
		this.exerciseId = exerciseId;
		this.createAt = currentTime();
		this.addObserver(observer);
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (currentTime() - createAt > VALID_TIME) {
					cancelTimer();
					setChanged();
					notifyObservers();
				}
			}
		}, 5000, 5000);
	}

	public void incitement() {
		this.createAt = currentTime();
	}

	public void cancelTimer() {
		this.timer.cancel();
	}

	private long currentTime() {
		return System.currentTimeMillis();
	}

	public String getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(String exerciseId) {
		this.exerciseId = exerciseId;
	}

}
