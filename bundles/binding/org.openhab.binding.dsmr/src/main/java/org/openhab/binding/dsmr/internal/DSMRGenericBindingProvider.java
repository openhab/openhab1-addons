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
package org.openhab.binding.dsmr.internal;

import org.openhab.binding.dsmr.DSMRBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author M.Volaart
 * @since 1.7.0
 */
public class DSMRGenericBindingProvider extends AbstractGenericBindingProvider implements DSMRBindingProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "dsmr";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // TODO: More advanced checking based on bindingConfig is possible
        if (!(item instanceof NumberItem) && !(item instanceof StringItem) && !(item instanceof DateTimeItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only NumberItems, DateTimeItems or StringItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        DSMRBindingConfig config = new DSMRBindingConfig();

        config.dsmrItemID = bindingConfig.trim();

        addBindingConfig(item, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDSMRItemID(String itemName) {
        DSMRBindingConfig config = (DSMRBindingConfig) bindingConfigs.get(itemName);

        if (config != null) {
            return config.dsmrItemID;
        } else {
            return null;
        }
    }

    /**
     * The DSMR binding configuration class.
     * <p>
     * The binding configuration consists only of the OBIS item.
     * <p>
     * Binding configuration for an openHAB Item looks like: dsmr="<OBIS item>"
     * 
     * @author M. Volaart
     * @since 1.7.0
     */
    class DSMRBindingConfig implements BindingConfig {
        public String dsmrItemID;
    }
}
