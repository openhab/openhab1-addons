/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
