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
package org.openhab.binding.myq.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.myq.MyqBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Scott Hanson
 * @since 1.8.0
 */
public class MyqGenericBindingProvider extends AbstractGenericBindingProvider implements MyqBindingProvider {
    static final Logger logger = LoggerFactory.getLogger(MyqGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "myq";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof RollershutterItem || item instanceof ContactItem
                || item instanceof StringItem || item instanceof NumberItem || item instanceof DateTimeItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only SwitchItems, RollershutterItem, ContactItem, NumberItem,"
                            + "DateTimeItem or StringItem are allowed " + "- please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        MyqBindingConfig config = parseBindingConfig(item, bindingConfig);

        addBindingConfig(item, config);
        super.processBindingConfiguration(context, item, bindingConfig);
    }

    /**
     * Parse item type to see what the action is used for
     */
    private MyqBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
        final MyqBindingConfig config = new MyqBindingConfig();

        config.acceptedDataTypes = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());

        logger.trace("bindingConfig: {}", bindingConfig);

        if (bindingConfig.contains("#")) {
            String[] newbindingConfig = bindingConfig.split("#");
            config.deviceIndex = Integer.parseInt(newbindingConfig[0]);
            config.attribute = newbindingConfig[1];
            logger.trace("deviceIndex: {} attribute: {}", config.deviceIndex, config.attribute);
        } else {
            config.deviceIndex = Integer.parseInt(bindingConfig);
            config.attribute = new String();
            logger.trace("deviceIndex: {} attribute: {}", config.deviceIndex, config.attribute);
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MyqBindingConfig getItemConfig(String itemName) {
        return (MyqBindingConfig) bindingConfigs.get(itemName);
    }

    public List<String> getInBindingItemNames() {
        List<String> inBindings = new ArrayList<String>();
        for (String itemName : bindingConfigs.keySet()) {
            inBindings.add(itemName);
        }
        return inBindings;
    }
}
