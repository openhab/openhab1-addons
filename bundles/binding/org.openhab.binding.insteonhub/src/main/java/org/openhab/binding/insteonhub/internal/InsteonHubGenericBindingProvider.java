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
package org.openhab.binding.insteonhub.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubGenericBindingProvider extends AbstractGenericBindingProvider
        implements InsteonHubBindingProvider {

    // map of itemNames configured for a device
    // key=deviceInfo, value=items
    private final Map<InsteonHubBindingDeviceInfo, Set<String>> deviceItems = new HashMap<InsteonHubBindingDeviceInfo, Set<String>>();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "insteonhub";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        if (bindingConfig != null) {
            // parse binding configuration
            InsteonHubBindingConfig config = InsteonHubBindingConfig.parse(item.getName(), bindingConfig);
            // add binding configuration
            addBindingConfig(item, config);
            // add to hubid+device map
            Set<String> deviceItems = getOrCreateDeviceItems(config.getDeviceInfo());
            deviceItems.add(config.getItemName());
        }
    }

    @Override
    public InsteonHubBindingConfig getItemConfig(String itemName) {
        return ((InsteonHubBindingConfig) bindingConfigs.get(itemName));
    }

    @Override
    public Set<InsteonHubBindingDeviceInfo> getConfiguredDevices() {
        Set<InsteonHubBindingDeviceInfo> ret = new LinkedHashSet<InsteonHubBindingDeviceInfo>();
        synchronized (deviceItems) {
            ret.addAll(deviceItems.keySet());
        }
        return ret;
    }

    @Override
    public Set<String> getDeviceItemNames(InsteonHubBindingDeviceInfo deviceInfo) {
        Set<String> ret = new LinkedHashSet<String>();
        synchronized (deviceItems) {
            Set<String> items = deviceItems.get(deviceInfo);
            if (items != null) {
                ret.addAll(items);
            }
        }
        return ret;
    }

    private Set<String> getOrCreateDeviceItems(InsteonHubBindingDeviceInfo deviceInfo) {
        synchronized (deviceItems) {
            Set<String> items = deviceItems.get(deviceInfo);
            if (items == null) {
                items = new HashSet<String>();
                deviceItems.put(deviceInfo, items);
            }
            return items;
        }
    }

}
