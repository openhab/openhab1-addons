/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal.model;

import java.util.Map;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digitaldan.jomnilinkII.MessageTypes.properties.AudioZoneProperties;

/**
 * AudioZones are the outputs on a connected audio system
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class AudioZone extends OmnilinkDevice {
	private static final Logger logger = LoggerFactory
			.getLogger(AudioZone.class);
	
	private AudioZoneProperties properties;
	private Map<Integer, AudioSource> audioSources;

	public AudioZone(AudioZoneProperties properties) {
		this.properties = properties;
	}

	@Override
	public AudioZoneProperties getProperties() {
		return properties;
	}

	public void setProperties(AudioZoneProperties properties) {
		this.properties = properties;
	}

	public Map<Integer, AudioSource> getAudioSources() {
		return audioSources;
	}

	public void setAudioSource(Map<Integer, AudioSource> audioSources) {
		this.audioSources = audioSources;
	}

	@Override
	public void updateItem(Item item, OmniLinkBindingConfig config,
			EventPublisher publisher) {
		int num = 0;
		String str = "";
		int source = new Integer(properties.getSource());

		switch (config.getObjectType()) {
		case AUDIOZONE_MUTE:
			num = properties.isMute() ? 1 : 0;
			break;
		case AUDIOZONE_POWER:
			num = properties.isOn() ? 1 : 0;
			break;
		case AUDIOZONE_SOURCE:
			num = properties.getSource();
			break;
		case AUDIOZONE_VOLUME:
			num = properties.getVolume();
			break;
		case AUDIOZONE_TEXT:
			if (sourceValid(source))
				str = audioSources.get(source).formatAudioText();
			break;
		case AUDIOZONE_TEXT_FIELD1:
			if (sourceValid(source))
				str = audioSources.get(source).getAudioText(0);
			break;
		case AUDIOZONE_TEXT_FIELD2:
			if (sourceValid(source))
				str = audioSources.get(source).getAudioText(1);
			break;
		case AUDIOZONE_TEXT_FIELD3:
			if (sourceValid(source))
				str = audioSources.get(source).getAudioText(2);
			break;
		case AUDIOZONE_KEY:
			num = -1;
			break;
		default:
			return;
		}

		if (item instanceof DimmerItem) {
			logger.debug("updating percent type {}", num);
			publisher.postUpdate(item.getName(), new PercentType(num));
		} else if (item instanceof NumberItem) {
			logger.debug("updating number type {}", num);
			publisher.postUpdate(item.getName(), new DecimalType(num));
		} else if (item instanceof SwitchItem) {
			logger.debug("updating switch type {}", num > 0 ? OnOffType.ON
					: OnOffType.OFF);
			publisher.postUpdate(item.getName(), num > 0 ? OnOffType.ON
					: OnOffType.OFF);
		} else if (item instanceof StringItem) {
			logger.debug("updating string type {}", str);
			publisher.postUpdate(item.getName(), new StringType(str));
		}
	}

	private boolean sourceValid(Integer source) {
		return audioSources != null && audioSources.containsKey(source);
	}
}
