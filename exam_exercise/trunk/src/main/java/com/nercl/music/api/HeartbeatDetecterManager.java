package com.nercl.music.api;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.nercl.music.service.ConsumeService;

@Component
public class HeartbeatDetecterManager implements Observer {

	@Autowired
	private ConsumeService consumeService;

	private static Map<String, Heartbeat> heartbeatPool = Maps.newConcurrentMap();

	public void newHeartbeatAndAddToPool(String exerciseId) {
		if (heartbeatPool.containsKey(exerciseId)) {
			return;
		}
		Heartbeat heartbeat = new Heartbeat(exerciseId, this);
		heartbeatPool.put(exerciseId, heartbeat);
	}

	public void removeHeartbeat(String exerciserId) {
		consumeService.endConsume(exerciserId);
		Heartbeat heartbeat = heartbeatPool.get(exerciserId);
		if (null != heartbeat) {
			heartbeat.cancelTimer();
		}
		heartbeatPool.remove(exerciserId);
	}

	public void incitement(String exerciserId) {
		Heartbeat heartbeat = heartbeatPool.get(exerciserId);
		if (null != heartbeat) {
			heartbeat.incitement();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Heartbeat heartbeat = (Heartbeat) o;
		String exerciserId = heartbeat.getExerciseId();
		removeHeartbeat(exerciserId);
	}

}
