/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.squeezebox.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.io.squeezeserver.SqueezePlayer;
import org.openhab.io.squeezeserver.SqueezeServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the methods that are made available in scripts and rules
 * for Squeezebox integration.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class Squeezebox {

	private static final Logger logger = 
		LoggerFactory.getLogger(Squeezebox.class);

	private final static String GOOGLE_TRANSLATE_URL = "http://translate.google.com/translate_tts?tl=en&q=";
	private final static int MAX_SENTENCE_LENGTH = 100;

	// handle to the Squeeze Server connection
	static SqueezeServer squeezeServer;
	
	@ActionDoc(text = "Send a notification to your Android device using the default api key", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean saySqueezebox(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say") String message) {
		
		// check the Squeeze Server has been initialised
		if (squeezeServer == null) {
			logger.error("Squeeze Server yet to be initialised.");
			return false;
		}
		
		// check we are connected to the Squeeze Server
		if (!squeezeServer.isConnected()){
			logger.error("Not connected to the Squeeze Server. Please check your config and consult the openHAB WIKI for instructions on how to configure the Squeezebox properties.");
			return false;
		}
		
		// get the player for this id
		SqueezePlayer player = squeezeServer.getPlayerByOhName(playerId);
		if (player == null) {
			logger.error("No player exists for id '{}'", playerId);
			return false;
		}

		// can only 'say' 100 chars at a time so split into sentences
		List<String> sentences = getSentences(message, MAX_SENTENCE_LENGTH);

		// send each sentence in turn
		for (String sentence : sentences) {
			logger.debug("Sending sentence to " + playerId + " (" + sentence + ")");
			
			String encodedSentence;
			try {
				encodedSentence = URLEncoder.encode(sentence, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warn("Failed to encode sentence '" + sentence + "', skipping.", e);
				continue;
			}
			encodedSentence = encodedSentence.replace("+", "%20");
			logger.trace("Encoded sentence " + encodedSentence);
			
			// send the request
			squeezeServer.sendCommand(player.getId() + " playlist play " + GOOGLE_TRANSLATE_URL + encodedSentence);
		}
		
		return true;
	}
	
	private static List<String> getSentences(String message, int maxSentenceLength) {
		// can only 'say' 100 chars at a time so split the message into words
		String[] words = StringUtils.split(message, ' ');

		List<String> sentences = new ArrayList<String>();
		String sentence = "";
		
		for (String word : words) {
			// ignore double spaces
			if (word.length() == 0) {
				continue;
			}
			
			// check this word isn't too long by itself
			if (word.length() > maxSentenceLength) {
				logger.warn("Unable to say '{}' as this word is longer than the maximum sentence allowed ({})", word, maxSentenceLength);
				continue;
			}
			
			// if this word makes our sentence too long start a new sentence
			if (sentence.length() + word.length() > maxSentenceLength) {
				sentences.add(sentence.substring(0, sentence.length() - 1));
				sentence = "";
			} 
			
			// add this word to the current sentence
			sentence += word + " ";
		}
		
		// add the final sentence
		if (sentence.length() > 0)
			sentences.add(sentence.substring(0, sentence.length() - 1));
		
		return sentences;
	}
}
