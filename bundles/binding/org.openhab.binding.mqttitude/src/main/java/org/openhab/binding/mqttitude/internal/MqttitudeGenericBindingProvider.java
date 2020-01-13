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
package org.openhab.binding.mqttitude.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mqttitude.MqttitudeBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration. A valid
 * items binding configuration file will look like the following:
 *
 * <pre>
 * Switch Person_A	"Person A"	{ mqttitude="<broker>:<topic>[:<region>]" }
 * Switch Person_B  "Person B"	{ mqttitude="mosquitto:/mqttitude/personB:home" }
 * </pre>
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class MqttitudeGenericBindingProvider extends AbstractGenericBindingProvider
        implements MqttitudeBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "mqttitude";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only Switch are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (StringUtils.isEmpty(bindingConfig)) {
            throw new BindingConfigParseException(
                    "Null config for " + item.getName() + " - expecting an Mqttitude topic");
        }

        addBindingConfig(item, new MqttitudeItemConfig(item.getName(), bindingConfig));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MqttitudeItemConfig getItemConfig(String itemName) {
        return (MqttitudeItemConfig) bindingConfigs.get(itemName);
    }
}
