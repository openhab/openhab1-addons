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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.VolumeControl;
import com.connectsdk.service.capability.VolumeControl.VolumeListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;

/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class VolumeControlVolume extends AbstractOpenhabConnectSDKPropertyBridge<VolumeListener> {
	private static final Logger logger = LoggerFactory.getLogger(VolumeControlVolume.class);

	@Override
	protected String getItemProperty() {
		return "volume";
	}

	@Override
	protected String getItemClass() {
		return "VolumeControl";
	}

	private VolumeControl getControl(final ConnectableDevice device) {
		return device.getCapability(VolumeControl.class);
	}

	@Override
	public void onReceiveCommand(final ConnectableDevice d, final String clazz, final String property, Command command) {
		if (matchClassAndProperty(clazz, property) && d.hasCapabilities(VolumeControl.Volume_Set)) {
			PercentType percent;
			if (command instanceof PercentType) {
				percent = (PercentType) command;
			} else if (command instanceof DecimalType) {
				percent = new PercentType(((DecimalType) command).toBigDecimal());
			} else if (command instanceof StringType) {
				percent = new PercentType(((StringType) command).toString());
			} else {
				logger.warn("only accept precentType");
				return;
			}

			getControl(d).setVolume(percent.floatValue() / 100.0f, createDefaultResponseListener());

		}

	}

	@Override
	protected ServiceSubscription<VolumeListener> getSubscription(final ConnectableDevice device,
			final Collection<String> itemNames, final EventPublisher eventPublisher) {
		if (device.hasCapability(VolumeControl.Volume_Subscribe)) {
			return getControl(device).subscribeVolume(new VolumeListener() {

				@Override
				public void onError(ServiceCommandError error) {
					logger.debug("error: {} {} {}", error.getCode(), error.getPayload(), error.getMessage());
				}

				@Override
				public void onSuccess(Float value) {
					if (eventPublisher != null) {
						for (String itemName : itemNames) {
							eventPublisher.postUpdate(itemName, new PercentType(Math.round(value * 100)));
						}
					}
				}
			});
		} else
			return null;
	}

}