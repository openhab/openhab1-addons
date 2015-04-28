/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.handler;

import static org.openhab.binding.ulux.UluxBindingConfigType.VIDEO;

import java.util.Map.Entry;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.ump.UluxMessageDatagram;
import org.openhab.binding.ulux.internal.ump.messages.VideoStateMessage;
import org.openhab.core.library.types.OnOffType;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
final class VideoStateMessageHandler extends AbstractMessageHandler<VideoStateMessage> {

	@Override
	public void handleMessage(VideoStateMessage message, UluxMessageDatagram response) {
		// video active
		for (Entry<String, UluxBindingConfig> entry : getBindingConfigs(VIDEO).entrySet()) {
			final OnOffType newState = message.isVideoActive() ? OnOffType.ON : OnOffType.OFF;

			this.eventPublisher.postUpdate(entry.getKey(), newState);
		}
	}

}
