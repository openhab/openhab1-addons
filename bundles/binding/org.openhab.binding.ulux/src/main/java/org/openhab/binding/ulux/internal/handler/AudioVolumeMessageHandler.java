/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.UluxBindingConfigType.AUDIO_VOLUME;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.AudioVolumeMessage;
import org.openhab.core.library.types.PercentType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class AudioVolumeMessageHandler extends AbstractMessageHandler<AudioVolumeMessage> {

	@Override
	public void handleMessage(AudioVolumeMessage message, UluxMessageDatagram response) {
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(AUDIO_VOLUME).entrySet()) {
			final String itemName = entry.getKey();

			this.eventPublisher.postUpdate(itemName, new PercentType(message.getVolume()));
		}
	}

}
