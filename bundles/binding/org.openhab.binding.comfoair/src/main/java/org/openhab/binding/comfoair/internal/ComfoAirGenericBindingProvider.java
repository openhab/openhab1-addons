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
package org.openhab.binding.comfoair.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.comfoair.ComfoAirBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * ComfoAir binding information from it. It registers as a
 * {@link ComfoAirBindingProvider} service as well.
 * </p>
 *
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{ comfoair="fan_level" }</code> - receives fan level</li>
 * <li><code>{ comfoair="bypass_mode" }</code> - receives bypass state</li>
 * </ul>
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public class ComfoAirGenericBindingProvider extends AbstractGenericBindingProvider implements ComfoAirBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "comfoair";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof NumberItem || item instanceof StringItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only Number- and StringItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        super.processBindingConfiguration(context, item, bindingConfig);
        ComfoAirBindingConfig config = new ComfoAirBindingConfig(bindingConfig);
        addBindingConfig(item, config);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public List<String> getConfiguredKeys() {
        Set<String> eventTypes = new HashSet<String>();

        Iterator<BindingConfig> it = bindingConfigs.values().iterator();
        while (it.hasNext()) {
            BindingConfig config = it.next();
            eventTypes.add(((ComfoAirBindingConfig) config).key);
        }

        return new ArrayList<String>(eventTypes);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public List<String> getItemNamesForCommandKey(String eventKey) {
        Set<String> itemNames = new HashSet<String>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            ComfoAirBindingConfig config = (ComfoAirBindingConfig) entry.getValue();
            if (config.key.equals(eventKey)) {
                itemNames.add(entry.getKey());
            }
        }
        return new ArrayList<String>(itemNames);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public String getConfiguredKeyForItem(String itemName) {
        return ((ComfoAirBindingConfig) bindingConfigs.get(itemName)).key;
    }

    /**
     * This is an internal data structure to store information from the binding
     * config strings.
     */
    class ComfoAirBindingConfig implements BindingConfig {
        public String key;

        public ComfoAirBindingConfig(String bindingConfig) {
            key = bindingConfig;
        }
    }

}