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
package org.openhab.binding.garadget.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of an openHAB item that is bound to a Garadget
 * device.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetBindingConfig implements BindingConfig {

    private final Logger logger = LoggerFactory.getLogger(GaradgetBindingConfig.class);

    private List<GaradgetPublisher> publishConfigurations = new ArrayList<GaradgetPublisher>();
    private List<GaradgetSubscriber> subscribeConfigurations = new ArrayList<GaradgetSubscriber>();

    /**
     * Create new Garadget binding configuration for the given item.
     *
     * @param item
     *            item
     * @param bindingConfig
     *            configuration string
     * @throws BindingConfigParseException
     *             If the configuration string is invalid.
     */
    public GaradgetBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {

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
                GaradgetSubscriber subscribeConfig = new GaradgetSubscriber(item, configContent);
                subscribeConfigurations.add(subscribeConfig);
            } else if (type.equals(">")) {
                GaradgetPublisher publishConfig = new GaradgetPublisher(item, configContent);
                publishConfigurations.add(publishConfig);
            } else {
                throw new BindingConfigParseException(
                        "Invalid Garadget binding configuration '" + configContent + "' for item " + item.getName());
            }
        }

        logger.debug("Loaded Garadget config for item '{}' : {} subscribers, {} publishers", item.getName(),
                subscribeConfigurations.size(), publishConfigurations.size());
    }

    /**
     * @return List of all defined publish configurations for the item.
     */
    public List<GaradgetPublisher> getPublishers() {
        return publishConfigurations;
    }

    /**
     * @return List of all defined subscribe configurations for the item.
     */
    public List<GaradgetSubscriber> getSubscribers() {
        return subscribeConfigurations;
    }

}
