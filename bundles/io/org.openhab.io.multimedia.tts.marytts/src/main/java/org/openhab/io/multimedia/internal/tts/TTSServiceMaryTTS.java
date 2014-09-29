/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.internal.tts;

import java.util.Locale;

import javax.sound.sampled.AudioInputStream;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.util.data.audio.AudioPlayer;

import org.apache.commons.lang.StringUtils;

import org.openhab.io.multimedia.tts.TTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a pure Java TTS service implementation, based on MaryTTS.
 * 
 * @author Tobias Bräutigam
 * @since 1.1.0
 * 
 */
public class TTSServiceMaryTTS implements TTSService {

	private static final Logger logger = LoggerFactory.getLogger(TTSServiceMaryTTS.class);

	private static MaryInterface marytts;
	private Voice defaultVoice;

	public void activate() {
		try {
			marytts = new LocalMaryInterface();
			Locale systemLocale = Locale.getDefault();
			if (marytts.getAvailableLocales().contains(systemLocale)) {
				defaultVoice = Voice.getDefaultVoice(systemLocale);
			}
			if (defaultVoice==null) {
				// Fallback
				defaultVoice = Voice.getVoice(marytts.getAvailableVoices().iterator().next());
			}
		} catch (MaryConfigurationException e) {
			logger.error("Error connecting to Mary TTS: " + e.getLocalizedMessage(), e);
		}
	}

	public void deactivate() {
		marytts = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void say(String text, String voiceName, String outputDevice) {
		if (marytts==null) {
			logger.error("Mary TTS is not available");
			return;
		}
		if(text==null) {
			return;
		}
		Voice voice=null;
		if (StringUtils.isBlank(voiceName)) {
            logger.debug("Mary TTS: {} (Voice not set. Using default voice {}).", new String[] { text, defaultVoice.toString() });
            voice = defaultVoice;
		} else {
            voice = Voice.getVoice(voiceName);
            logger.debug("Mary TTS: {} (Voice: {})", new String[] { text, voiceName });
		}
		
		if (voice != null) {
			// Workaround: we have to set the Locale first, because only in the LocalMaryInterface.setLocale() method the required private method
			// LocalMaryInterface.setAudioFileFormatForVoice() method is called. After that we can set the voice, otherwise an NPE occurs
			marytts.setLocale(voice.getLocale());
			marytts.setVoice(voice.getName());
			try {
				AudioInputStream audio = marytts.generateAudio(text);
				AudioPlayer player = new AudioPlayer(audio);
				player.start();
				player.join();
				
			} catch (SynthesisException e) {
				logger.error("Error during tts generation: {}", e.getLocalizedMessage(), e);
			} catch (InterruptedException e) {
				logger.error("Error during tts playback: {}", e.getLocalizedMessage(), e);
			}
		}
		else {
			logger.error("Could not find voice: {}", voiceName);
			logger.info("Available Voices are {} ", StringUtils.join(marytts.getAvailableVoices(), ", "));
		}
	}
}
