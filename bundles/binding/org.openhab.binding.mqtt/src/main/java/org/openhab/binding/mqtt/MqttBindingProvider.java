/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
