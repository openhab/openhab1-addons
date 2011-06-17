/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
	
	static {
		for(Voice voice : new KevinVoiceDirectory().getVoices()) {
			voices.put(voice.getName(), voice);
		}
		for(Voice voice : new AlanVoiceDirectory().getVoices()) {
			voices.put(voice.getName(), voice);
		}
	}
	
	
	public void activate() {
	}
	
	public void deactivate() {
		for(Voice voice : voices.values()) {
			voice.deallocate();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void speak(String text, String voiceName) {

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
