/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal.model;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;

import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioSourceProperties;

/**
 * Audio Sources are the numbered input found in AudioZones
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class AudioSource extends OmnilinkDevice {
	private AudioSourceProperties properties;
	private String[] audioText;

	public AudioSource(AudioSourceProperties properties) {
		this.properties = properties;
		this.audioText = new String[0];
	}

	@Override
	public AudioSourceProperties getProperties() {
		return properties;
	}

	public void setProperties(AudioSourceProperties properties) {
		this.properties = properties;
	}

	/**
	 * Returns the display text for this audio source.
	 * @return array of audio fields 
	 */
	public String[] getAudioText() {
		return audioText;
	}

	/**
	 * Sets the display text for this audio source.
	 * @param audioText
	 */
	public void setAudioText(String[] audioText) {
		this.audioText = audioText;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		String str = " ";

		switch (config.getObjectType()) {

		case AUDIOSOURCE_TEXT:
			str = formatAudioText();
			break;
		case AUDIOSOURCE_TEXT_FIELD1:
			str = getAudioText(0);
			break;
		case AUDIOSOURCE_TEXT_FIELD2:
			str = getAudioText(1);
			break;
		case AUDIOSOURCE_TEXT_FIELD3:
			str = getAudioText(2);
			break;
		default:
			return;
		}

		if (item instanceof StringItem) {
			publisher.postUpdate(item.getName(), new StringType(str));
		}
	}

	/**
	 * Formats the multiple lines of audio text into a single delimited string.
	 * @return pipe delimited audio text string.
	 */
	public String formatAudioText() {
		StringBuilder sb = new StringBuilder();
		for (String s : audioText) {
			if (sb.length() > 0)
				sb.append(" | ");
			sb.append(s);
		}
		if (sb.length() == 0)
			sb.append(" ");

		return sb.toString();
	}

	public String getAudioText(int fieldNum) {
		String ret = fieldNum < audioText.length ? audioText[fieldNum] : null;
		return ret != null ? ret : " ";
	}
}
