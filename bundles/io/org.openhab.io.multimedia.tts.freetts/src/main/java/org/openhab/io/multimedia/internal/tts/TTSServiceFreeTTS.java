/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.util.HashMap;
import java.util.Map;

import org.openhab.io.multimedia.tts.TTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory;
import com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory;

/**
 * This is a pure Java TTS service implementation, based on FreeTTS.
 * 
 * @author Kai Kreuzer
 * @since 0.8.0
 *
 */
public class TTSServiceFreeTTS implements TTSService {

	private static final Logger logger = LoggerFactory.getLogger(TTSServiceFreeTTS.class);
	
	private static final Map<String, Voice> voices = new HashMap<String, Voice>();
		
	public void activate() {
		for(Voice voice : new KevinVoiceDirectory().getVoices()) {
			voices.put(voice.getName(), voice);
		}
		for(Voice voice : new AlanVoiceDirectory().getVoices()) {
			voices.put(voice.getName(), voice);
		}
		
		// workaround for JVM bug, see
		// http://ondra.zizka.cz/stranky/programovani/java/misc/freetts-line-unavailable-classcastexception-kevinvoicedirectory-error-opening-zipfile.texy
		System.setProperty("com.sun.speech.freetts.audio.AudioPlayer.openFailDelayMs", "100");
		System.setProperty("com.sun.speech.freetts.audio.AudioPlayer.totalOpenFailDelayMs", "30000");

	}
	
	public void deactivate() {
		for(Voice voice : voices.values()) {
			voice.deallocate();
		}
		voices.clear();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void say(String text, String voiceName, String outputDevice) {

		if(text==null) {
			return;
		}
		
		if(voiceName==null) {
			voiceName = "kevin16";
		}

		Voice voice = voices.get(voiceName);

		if(voice!=null) {
			if(!voice.isLoaded()) {
				voice.allocate();
			}
			voice.speak(text);
		} else {
			logger.error("Could not find voice: " + voiceName);
			StringBuilder sb = new StringBuilder();
			if(logger.isInfoEnabled()) {
				for(String name : voices.keySet()) {
					sb.append(name + " ");
				}
				logger.info("Available voices are: [ {}]", sb.toString());
			}
		}
	}
	
}
