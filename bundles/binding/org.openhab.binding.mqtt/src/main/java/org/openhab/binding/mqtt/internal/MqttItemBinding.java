/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.mqtt.internal;

import org.openhab.binding.mqtt.MqttBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT binding to add MQTT send and receive functionality to OpenHab items.
 * This binding receives all commands and state updates and uses the item
 * configuration to publish them to MQTT.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttItemBinding extends AbstractBinding<MqttBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(MqttItemBinding.class);

	/**
	 * @return MqttBindingProvider implementation.
	 */
	private MqttBindingProvider getBindingProvider() {
		return providers.iterator().next();
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		MqttItemConfig itemConfig = getBindingProvider().getItemConfig(itemName);

		for (MqttMessagePublisher publisher : itemConfig.getMessagePublishers()) {
			if (publisher.supportsCommand(command)) {
				logger.debug("Publishing command {} to {}", command.toString(), publisher.getTopic());
				publisher.publish(publisher.getTopic(), command.toString().getBytes());
			}
		}
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {

		MqttItemConfig itemConfig = getBindingProvider().getItemConfig(itemName);

		for (MqttMessagePublisher publisher : itemConfig.getMessagePublishers()) {
			if (publisher.supportsState(newState)) {
				logger.debug("Publishing state {} to {}", newState.toString(), publisher.getTopic());
				publisher.publish(publisher.getTopic(), newState.toString().getBytes());
			}
		}
	}

}
