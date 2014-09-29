/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.config;

import org.openhab.binding.dmx.DmxBindingProvider;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.internal.core.DmxChannel;
import org.openhab.binding.dmx.internal.core.DmxUtil;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DMX item configuration for openHAB Dimmer Items.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxDimmerItem extends DmxSwitchItem {

	/** Step size in a 0-100 scale for increasing/decreasing dim values */
	protected static final int DIMMER_STEP_SIZE = 5;

	/**
	 * Create new dimmer item using a given configuration string.
	 * 
	 * @param itemName
	 *            name of the item
	 * @param configString
	 *            configuration string
	 * @param dmxBindingProvider
	 *            binding provider which created the item
	 * @throws BindingConfigParseException
	 *             if configuration string could not be parsed.
	 */
	public DmxDimmerItem(String itemName, String configString,
			DmxBindingProvider dmxBindingProvider)
			throws BindingConfigParseException {
		super(itemName, configString, dmxBindingProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processCommand(DmxService service, Command command) {

		// process increase/decrease
		if (command instanceof IncreaseDecreaseType
				&& !isRedefinedByCustomCommand(command)) {
			IncreaseDecreaseType t = (IncreaseDecreaseType) command;

			if (IncreaseDecreaseType.INCREASE.equals(t)) {
				for (int channelId : channels) {
					service.enableChannel(channelId);
					service.increaseChannel(channelId, DIMMER_STEP_SIZE);
					if (service.getChannelValue(channelId) == 0) {
						service.setChannelValue(channelId,
								DmxChannel.DMX_MAX_VALUE);
					}
				}
			} else {
				for (int channelId : channels) {
					service.decreaseChannel(channelId, DIMMER_STEP_SIZE);
				}
			}
			return;
		}

		// process percent command
		if (command instanceof PercentType && !isRedefinedByCustomCommand(command)) {
			for (int channelId : channels) {
				service.setChannelValue(channelId, DmxChannel.DMX_MAX_VALUE);
				service.setChannelValue(channelId, (PercentType) command);
			}
			return;
		}

		// process switch command
		super.processCommand(service, command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processStatusUpdate(int[] channelValues) {

		if (channelValues.length == 0) {
			return;
		}

		publishState(DmxUtil.getPercentTypeFromByte(channelValues[0]));
	}
}
