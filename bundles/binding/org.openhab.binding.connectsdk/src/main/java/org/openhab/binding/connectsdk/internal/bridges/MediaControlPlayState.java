/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.MediaControl;
import com.connectsdk.service.capability.MediaControl.PlayStateListener;
import com.connectsdk.service.capability.MediaControl.PlayStateStatus;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;

// TODO: somehow this does not work with LG 60UF7709 spotify app or netflix

/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class MediaControlPlayState extends AbstractOpenhabConnectSDKPropertyBridge<PlayStateListener> {
	private static final Logger logger = LoggerFactory.getLogger(MediaControlPlayState.class);

	@Override
	protected String getItemClass() {
		return "MediaControl";
	}

	@Override
	protected String getItemProperty() {
		return "playState";
	}

	private MediaControl getControl(final ConnectableDevice device) {
		return device.getCapability(MediaControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {

	}

	@Override
	protected ServiceSubscription<PlayStateListener> getSubscription(final ConnectableDevice device,
			final Collection<String> itemNames, final EventPublisher eventPublisher) {
		if (device.hasCapability(MediaControl.PlayState_Subscribe)) {
			return getControl(device).subscribePlayState(new PlayStateListener() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.debug("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
				}

				@Override
				public void onSuccess(PlayStateStatus status) {
					if (eventPublisher != null) {
						for (String itemName : itemNames) {
							eventPublisher.postUpdate(itemName, new StringType(status.name()));
						}
					}
				}
			});
		} else
			return null;
	}

}