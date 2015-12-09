/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import speechd.ssip.SSIPClient;
import speechd.ssip.SSIPException;
import speechd.ssip.SSIPPriority;

/**
 * This class open a TCP/IP connection to the Speech Dispatcher service and can
 * send messages to be announced
 * 
 * @author Gaël L'hopital
 * @since 1.6.0
 */

public class SpeechDispatcherConnection {

	private static Logger logger = LoggerFactory
			.getLogger(SpeechDispatcherConnection.class);

	public String port;
	public String host;

	private static SSIPClient speechDispatcherClient = null;

	public SpeechDispatcherConnection() {
	}

	public void openConnection() {
		closeConnection();
		try {
			speechDispatcherClient = new SSIPClient("openhab", null, null,
					host, port);
		} catch (SSIPException e) {
			logger.error("Error connecting to SpeechDispatcher Host {}: {}",
					host, e.getLocalizedMessage());
		}
	}

	public void closeConnection() {
		if (speechDispatcherClient != null) {
			try {
				speechDispatcherClient.close();
				speechDispatcherClient = null;
			} catch (SSIPException e) {
				logger.error(
						"Error closing connection to SpeechDispatcher Host {}: {}",
						host, e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Speaks the text with a given voice
	 *
	 * @param text
	 *            the text to speak
	 * @param voice
	 *            the name of the voice to use or null, if the default voice
	 *            should be used
	 */
	public void say(String text, String voiceName) {
		if (text == null) {
			return;
		}

		openConnection();
		try {
			if (voiceName != null) {
				speechDispatcherClient.setVoice(voiceName);
			}
			speechDispatcherClient.say(SSIPPriority.MESSAGE, text);
		} catch (SSIPException e) {
			logger.error("Error sending text to SpeechDispatcher Host {}: {}",
					host, e.getLocalizedMessage());
		}
		closeConnection();

	}

	@Override
	public String toString() {
		return "Device [host=" + host + ", port=" + port + "]";
	}

}
