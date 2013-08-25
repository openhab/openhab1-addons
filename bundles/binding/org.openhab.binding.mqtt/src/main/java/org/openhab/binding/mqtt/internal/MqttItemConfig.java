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
