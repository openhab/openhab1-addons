/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqtt.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT configuration for an openHAB item. An item's MQTT configuration can
 * contain multiple inbound or outbound message configurations.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttItemConfig implements BindingConfig {

	private static final Logger logger = LoggerFactory.getLogger(MqttItemConfig.class);

	private List<MqttMessagePublisher> publishConfigurations = new ArrayList<MqttMessagePublisher>();

	private List<MqttMessageSubscriber> subscribeConfigurations = new ArrayList<MqttMessageSubscriber>();

	/**
	 * Create new MQTT binding configuration for the given item.
	 * 
	 * @param itemName
	 *            item name.
	 * @param bindingConfig
	 *            configuration string
	 * @throws BindingConfigParseException
	 *             If the configuration string is invalid.
	 */
	public MqttItemConfig(String itemName, String bindingConfig) throws BindingConfigParseException {

		String[] configurationStrings = bindingConfig.split("],");

		for (String config : configurationStrings) {
			config = config.trim();
			String type = config.substring(0, 1);
			int firstPos = config.indexOf('[');
			int lastPos = config.lastIndexOf(']');
			
			String configContent = config.substring(firstPos + 1);
			if (lastPos != -1) {
				configContent = config.substring(firstPos + 1, lastPos);
			}
			
			if (type.equals("<")) {
				MqttMessageSubscriber subscribeConfig = new MqttMessageSubscriber(configContent);
				subscribeConfigurations.add(subscribeConfig);
			} else if (type.equals(">")) {
				MqttMessagePublisher publishConfig = new MqttMessagePublisher(configContent);
				publishConfigurations.add(publishConfig);
			} else {
				throw new BindingConfigParseException(
					"Invalid mqtt binding configuration '" + configContent + "' for item " + itemName);
			}
		}

		logger.debug(
				"Loaded MQTT config for item '{}' : {} subscribers, {} publishers",
				new Object[] { itemName, subscribeConfigurations.size(),
						publishConfigurations.size() });
	}

	/**
	 * @return List of all defined publish configurations for the item.
	 */
	public List<MqttMessagePublisher> getMessagePublishers() {
		return publishConfigurations;
	}

	/**
	 * @return List of all defined subscribe configurations for the item.
	 */
	public List<MqttMessageSubscriber> getMessageSubscribers() {
		return subscribeConfigurations;
	}
	
}
