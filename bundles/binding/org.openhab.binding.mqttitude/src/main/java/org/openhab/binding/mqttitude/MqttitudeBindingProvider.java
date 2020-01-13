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
