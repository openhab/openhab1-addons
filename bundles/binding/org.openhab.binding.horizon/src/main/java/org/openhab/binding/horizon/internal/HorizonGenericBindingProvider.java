/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.horizon.internal;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.horizon.HorizonBindingProvider;
import org.openhab.binding.horizon.internal.control.Key;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Jurgen Kuijpers
 * @since 1.9.0
 */
public class HorizonGenericBindingProvider extends AbstractGenericBindingProvider implements HorizonBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "horizon";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof DimmerItem
                || item instanceof RollershutterItem || item instanceof StringItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch, Number, Dimmer, Rollershutter and String are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        HorizonBindingConfig config = new HorizonBindingConfig();
        parseBindingConfig(bindingConfig, config);
        addBindingConfig(item, config);
        super.processBindingConfiguration(context, item, bindingConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHorizonCommand(String itemName, String command) {
        HorizonBindingConfig config = (HorizonBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.get(command) : null;
    }

    private void parseBindingConfig(String bindingConfigs, HorizonBindingConfig config)
            throws BindingConfigParseException {
        String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
        String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");
        String[] configParts = bindingConfig.trim().split(":");
        if (configParts.length != 3) {
            throw new BindingConfigParseException("Horizon binding must contain three parts separated by ':'");
        }

        String command = StringUtils.trim(configParts[0]);
        String horizonId = StringUtils.trim(configParts[1]);
        String horizonCommand = StringUtils.trim(configParts[2]);

        Integer key = Key.getByName(horizonCommand);

        if (key == null) {
            throw new BindingConfigParseException("Unregonized value '" + horizonCommand + "'");
        }

        // if there are more commands to parse do that recursively ...
        if (StringUtils.isNotBlank(bindingConfigTail)) {
            parseBindingConfig(bindingConfigTail, config);
        }
        config.put(command, horizonId + ":" + key);
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Jurgen Kuijpers
     * @since 1.9.0
     */
    private class HorizonBindingConfig extends HashMap<String, String> implements BindingConfig {
        private static final long serialVersionUID = 1L;
    }

}
