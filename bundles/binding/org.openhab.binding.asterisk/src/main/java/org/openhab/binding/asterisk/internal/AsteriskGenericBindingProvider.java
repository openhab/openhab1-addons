/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.asterisk.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.asterisk.AsteriskBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and
 * provides Asterisk binding information from it. It registers as a
 * {@link AsteriskBindingProvider} service as well.
 * </p>
 *
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>{ asterisk="active" }</code> - receives status updates on active calls</li>
 * </ul>
 * These binding configurations can be used on either SwitchItems or CallItems.
 * For SwitchItems, it will obviously receive ON at the beginning and OFF at the
 * end. CallItems will be filled with origination and the destination number.
 * </p>
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class AsteriskGenericBindingProvider extends AbstractGenericBindingProvider implements AsteriskBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(AsteriskGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "asterisk";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof CallItem || item instanceof SwitchItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only Call- and SwitchItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig != null) {
            AsteriskBindingConfig config = parseBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else {
            logger.warn("bindingConfig is NULL (item={}) -> processing bindingConfig aborted!", item);
        }
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an
     * appropriate instance.
     * 
     * @param item
     * @param bindingConfig
     * 
     * @throws BindingConfigParseException if bindingConfig is no valid binding type
     */
    protected AsteriskBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        logger.debug("Binding config: {}", bindingConfig);
        try {
            return new AsteriskBindingConfig(item.getClass(), bindingConfig);
        } catch (IllegalArgumentException iae) {
            throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
        }
    }

    public Class<? extends Item> getItemType(String itemName) {
        AsteriskBindingConfig config = (AsteriskBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.itemType : null;
    }

    public String getType(String itemName) {
        AsteriskBindingConfig config = (AsteriskBindingConfig) bindingConfigs.get(itemName);
        return config.type;
    }

    @Override
    public AsteriskBindingConfig getConfig(String name) {
        return (AsteriskBindingConfig) bindingConfigs.get(name);
    }

    public String[] getItemNamesByType(String type) {
        Set<String> itemNames = new HashSet<String>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            AsteriskBindingConfig fbConfig = (AsteriskBindingConfig) entry.getValue();
            if (fbConfig.type.equals(type)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    public class AsteriskBindingConfig implements BindingConfig {

        final Class<? extends Item> itemType;
        final String type;

        private final String callerId; // if null, any callerId is accepted
        private final String extension; // if null, any extension is accepted
        private final String digit;

        public AsteriskBindingConfig(Class<? extends Item> itemType, String config) throws BindingConfigParseException {
            this.itemType = itemType;

            String[] splitConfig = config.split(":");

            String eventType = splitConfig[0];

            if (eventType.equals("active")) {
                if (splitConfig.length == 1) {
                    this.type = eventType;
                    this.callerId = null;
                    this.extension = null;
                    this.digit = null;
                } else if (splitConfig.length == 3) {
                    this.type = eventType;
                    this.callerId = validateExtParam(splitConfig[1]);
                    this.extension = validateExtParam(splitConfig[2]);
                    this.digit = null;
                } else {
                    throw new BindingConfigParseException("Invalid number of parameters for eventType 'active'");
                }
            } else if (eventType.equals("digit")) {
                if (splitConfig.length == 4) {
                    this.type = eventType;
                    this.callerId = validateExtParam(splitConfig[1]);
                    this.extension = validateExtParam(splitConfig[2]);

                    String sDigit = splitConfig[3];
                    if (sDigit.length() == 1) {
                        if (sDigit.matches("[0-9*#]")) {
                            this.digit = sDigit;
                        } else {
                            throw new BindingConfigParseException("Invalid digit for eventType 'digit'");
                        }
                    } else {
                        throw new BindingConfigParseException("Only 1 digit can be specified for eventType 'digit'");
                    }
                } else {
                    throw new BindingConfigParseException("Invalid number of parameters for eventType 'digit'");
                }
            } else {
                throw new BindingConfigParseException("Unknown eventType");
            }

        }

        private String validateExtParam(String s) {
            String res = null;
            if (s.length() > 0) {
                if (s.equals("*")) {
                    res = null;
                } else {
                    res = s;
                }
            }
            return res;
        }

        public String getCallerId() {
            return this.callerId;
        }

        public String getExtension() {
            return this.extension;
        }

        public String getDigit() {
            return this.digit;
        }
    }
}
