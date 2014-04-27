/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
