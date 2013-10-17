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

import org.apache.commons.lang.NullArgumentException;
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

	@ActionDoc(text = "Speak a message via one of your Squeezebox devices using the current volume for that device", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean saySqueezebox(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say (max 100 chars)") String message) {
		return saySqueezebox(playerId, message, -1);
	}		
	
	@ActionDoc(text = "Speak a message via one of your Squeezebox devices using the specified volume", returns = "<code>true</code>, if successful and <code>false</code> otherwise.")
	public static boolean saySqueezebox(
			@ParamDoc(name = "playerId", text = "The Squeezebox to send the message to") String playerId,
			@ParamDoc(name = "message", text = "The message to say (max 100 chars)") String message,
			@ParamDoc(name = "volume", text = "The volume to set the device when speaking this message (between 1-100)") int volume) {
		
		if (StringUtils.isEmpty(playerId))
			throw new NullArgumentException("playerId");
		if (StringUtils.isEmpty(message))
			throw new NullArgumentException("message");

		// check the Squeeze Server has been initialised
		if (squeezeServer == null) {
			logger.error("Squeeze Server yet to be initialised.");
			return false;
		}
		
		// check we are connected to the Squeeze Server
		if (!squeezeServer.isConnected()) {
			logger.error("Not connected to the Squeeze Server. Please check your config and consult the openHAB WIKI for instructions on how to configure the Squeezebox properties.");
			return false;
		}

		SqueezePlayer player = squeezeServer.getPlayer(playerId);
		if (player == null) {
			logger.error("No Squeezebox player exists with name '{}'.", playerId);
			return false;
		}

		// get the current player state
		int playerVolume = player.getUnmuteVolume();
		boolean playerPowered = player.isPowered();
		boolean playerMuted = player.isMuted();
		
		// set the volume
		squeezeServer.setVolume(playerId, volume == -1 ? playerVolume : volume);

		// can only 'say' 100 chars at a time
		List<String> sentences = getSentences(message, MAX_SENTENCE_LENGTH);

		// send each sentence in turn
		for (String sentence : sentences) {
			logger.debug("Sending sentence to " + playerId + " (" + sentence + ")");
			
			String encodedSentence;
			try {
				encodedSentence = URLEncoder.encode(sentence, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warn("Failed to encode sentence '" + sentence + "'.", e);
				return false;
			}
			encodedSentence = encodedSentence.replace("+", "%20");
			logger.trace("Encoded sentence " + encodedSentence);
			
			// build the URL to send to the Squeezebox to play
			String url = GOOGLE_TRANSLATE_URL + encodedSentence;
			
			// create an instance of our special listener so we can detect when the sentence is complete
			SqueezeboxListener listener = new SqueezeboxListener(playerId, url);
			player.addPlayerEventListener(listener);
			
			// send the URL (this will power up the player and un-mute if necessary)
			squeezeServer.playUrl(playerId, url);
			
			// wait for this message to complete
			while (!listener.isFinished()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
			
			// clean up the listener
			player.removePlayerEventListener(listener);
			listener = null;
		}
		
		// restore the player state
		if (volume != -1)
			squeezeServer.setVolume(playerId, playerVolume);
		if (playerMuted)
			squeezeServer.mute(playerId);
		if (!playerPowered)
			squeezeServer.powerOff(playerId);
		
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
