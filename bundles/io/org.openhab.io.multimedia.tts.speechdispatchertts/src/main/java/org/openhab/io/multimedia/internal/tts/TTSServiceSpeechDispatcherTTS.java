/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import org.openhab.io.multimedia.tts.TTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import speechd.ssip.SSIPClient;
import speechd.ssip.SSIPException;
import speechd.ssip.SSIPPriority;

/**
 * This is a pure Java TTS service implementation, based on SpeechDispatcherTTS.
 * 
 * @author Gaël L'hopital
 * @since 1.6.0
 * 
 */
public class TTSServiceSpeechDispatcherTTS implements TTSService {

	private static final Logger logger = LoggerFactory.getLogger(TTSServiceSpeechDispatcherTTS.class);
	private static SSIPClient speechDispatcherClient;

	public void activate() {		
		try {
			speechDispatcherClient = new SSIPClient("openhab", null, null);
		} catch (SSIPException e) {
			logger.error("Error connecting to SpeechDispatcher TTS: " + e.getLocalizedMessage(), e);
		}
	}

	public void deactivate() {
		if (speechDispatcherClient != null) {
			try {
				speechDispatcherClient.close();
				speechDispatcherClient =null;
			} catch (SSIPException e) {
				logger.error("Error closing connection to SpeechDispatcher TTS: " + e.getLocalizedMessage(), e);
			}		
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void say(String text, String voiceName, String outputDevice) {
		if (speechDispatcherClient==null) {
			logger.error("SpeechDispatcher TTS is not available");
			return;
		}
		if(text==null) {
			return;
		}
		try {
			speechDispatcherClient.say(SSIPPriority.MESSAGE, text);
		} catch (SSIPException e) {
			logger.error("Error sending text to SpeechDispatcher TTS: " + e.getLocalizedMessage(), e);
		}
	}
}
