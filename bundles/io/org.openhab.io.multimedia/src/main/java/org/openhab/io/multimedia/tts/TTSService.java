/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.multimedia.tts;

/**
 * This is the interface that a text-to-speech service has to implement.
 * 
 * @author Kai Kreuzer
 * @since 0.8.0
 *
 */
public interface TTSService {

	/**
	 * Speaks the text with a given voice
	 * 
	 * @param text the text to speak
	 * @param voice the name of the voice to use or null, if the default voice should be used
	 * @param device the name of audio device to be used to play the audio or null, if the default output device should be used
	 */
	void say(String text, String voice, String outputDevice);

}
