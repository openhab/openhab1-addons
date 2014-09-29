/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqtt;

import org.openhab.binding.mqtt.internal.MqttItemConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * MQTT Binding Provider for items with MQTT configuration.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface MqttBindingProvider extends BindingProvider {

	/**
	 * Retrieve MQTT configuration for the item with the given ItemName.
	 * 
	 * @param itemName
	 *            name of the item for which to retrieve the config.
	 * @return item config.
	 */
	public MqttItemConfig getItemConfig(String itemName);

}
