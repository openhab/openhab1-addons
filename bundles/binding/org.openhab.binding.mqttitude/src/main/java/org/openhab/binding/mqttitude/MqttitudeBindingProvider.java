/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqttitude;

import org.openhab.binding.mqttitude.internal.MqttitudeItemConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * A custom binding provided for Mqttitude integration, to allow presence
 * detection using the Mqttitude service.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public interface MqttitudeBindingProvider extends BindingProvider {

	/**
	 * Returns the Mqttitude config specified for item {@code itemName}.
	 */
	public MqttitudeItemConfig getItemConfig(String itemName);
}
