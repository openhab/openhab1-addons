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
import org.openhab.binding.dmx.internal.cmd.DmxCommand;
import org.openhab.binding.dmx.internal.core.DmxChannel;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * DMX item configuration for openHAB Switch Items.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxSwitchItem extends DmxItem {

	/**
	 * Create new switch item using a given configuration string.
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
	public DmxSwitchItem(String itemName, String configString,
			DmxBindingProvider dmxBindingProvider)
			throws BindingConfigParseException {
		super(itemName, configString, dmxBindingProvider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processCommand(DmxService service, Command command) {

		// process regular on/off switch
		if (command instanceof OnOffType
				&& !isRedefinedByCustomCommand(command)) {

			if (OnOffType.ON.equals(command)) {
				boolean hasValue = false;
				for (int channelId : channels) {
					service.enableChannel(channelId);
					if (service.getChannelValue(channelId) > 0) {
						hasValue = true;
					}
				}
				if (!hasValue) {
					// switch to max value if there is no light..
					for (int channelId : channels) {
						service.setChannelValue(channelId,
								DmxChannel.DMX_MAX_VALUE);
					}
				}
			} else {
				for (int channelId : channels) {
					service.disableChannel(channelId);
				}
			}

			return;
		}

		// process custom commands if they are available
		if (isRedefinedByCustomCommand(command)) {
			DmxCommand dmxCommand = customCommands.get(command.toString());
			dmxCommand.execute(service);
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processStatusUpdate(int[] channelValues) {

		if (channelValues.length > 0 && channelValues[0] > 0) {
			publishState(OnOffType.ON);
		} else {
			publishState(OnOffType.OFF);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStatusListener() {
		if (updateDelay > MIN_UPDATE_DELAY && channels.length == 1) {
			return true;
		}
		return false;
	}

}
