/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.intertechno.CULIntertechnoBindingProvider;
import org.openhab.binding.intertechno.IntertechnoBindingConfig;
import org.openhab.binding.intertechno.internal.parser.AddressParserFactory;
import org.openhab.binding.intertechno.internal.parser.IntertechnoAddressParser;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULIntertechnoGenericBindingProvider extends AbstractGenericBindingProvider
        implements CULIntertechnoBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "culintertechno";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only SwitchItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * config of style
     * <code>{{@literal culintertechno="type=<classic|fls|rev>;group=<group>;address=<address>"}}</code><br>
     * 
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        String[] configParts = bindingConfig.split(";");
        String type = "";
        List<String> params = new ArrayList<String>();

        // extract the value of "type" parameter and put all other into the params array
        for (int i = 0; i < configParts.length; i++) {
            String paramName = configParts[i].split("=")[0].toLowerCase();

            if (paramName.equals("type")) {
                type = configParts[i].split("=")[1];
            } else {
                params.add(configParts[i]);
            }
        }

        if (StringUtils.isBlank(type)) {
            throw new BindingConfigParseException("'type' is missing in configuration!");
        }

        IntertechnoAddressParser parser = AddressParserFactory.getParser(type);
        parser.parseConfig(params);
        String commandOn = parser.getCommandON();
        String commandOff = parser.getCommandOFF();

        IntertechnoBindingConfig config = new IntertechnoBindingConfig(commandOn, commandOff);

        addBindingConfig(item, config);
    }

    @Override
    public IntertechnoBindingConfig getConfigForItemName(String itemName) {
        return (IntertechnoBindingConfig) bindingConfigs.get(itemName);
    }

}
