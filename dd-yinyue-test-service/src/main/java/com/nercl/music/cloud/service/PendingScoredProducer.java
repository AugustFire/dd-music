package com.nercl.music.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PendingScoredProducer {

	private static final String SHORT_PENGDING_SCORED_QUEUE = "short.pengding.scored.queue";

	private static final String SING_PENGDING_SCORED_QUEUE = "sing.pengding.scored.queue";

	private static final String CREATING_PENGDING_SCORED_QUEUE = "creating.pengding.audioed.queue";

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	public void sendShortScoredMessage(final String message) {
		jmsTemplate.convertAndSend(SHORT_PENGDING_SCORED_QUEUE, message);
	}

	public void sendSingScoredMessage(final String message) {
		jmsTemplate.convertAndSend(SING_PENGDING_SCORED_QUEUE, message);
	}

	public void sendCreatingAudiodMessage(final String message) {
		jmsTemplate.convertAndSend(CREATING_PENGDING_SCORED_QUEUE, message);
	}

}
