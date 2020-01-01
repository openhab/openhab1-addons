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
package org.openhab.binding.urtsi.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.urtsi.UrtsiBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The binding provider is responsible for defining some metadata of the binding and for providing a parser for the
 * binding configuration.
 *
 * @author Oliver Libutzki
 * @authot John Cocula -- translated to Java
 * @since 1.3.0
 *
 */
public class UrtsiGenericBindingProvider extends AbstractGenericBindingProvider implements UrtsiBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(UrtsiGenericBindingProvider.class);

    static final Pattern CONFIG_BINDING_PATTERN = Pattern.compile("(.*?):([0-9]*)((:)?([0-9])?)");

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "urtsi";
    }

    /**
     * The methods checks if the item which uses the urtsi binding is a rollershutter item.
     *
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof RollershutterItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "', only RollershutterItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * The Urtsi binding doesn't use auto-Update as we first check if the command is executed successfully.
     *
     * @see UrtsiBinding#internalReceiveCommand(String, org.openhab.core.types.Command)
     */
    @Override
    public Boolean autoUpdate(String itemName) {
        if (providesBindingFor(itemName)) {
            return false;
        } else {
            return null;
        }
    }

    /**
     * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
     *
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        if (bindingConfig != null) {
            parseAndAddBindingConfig(item, bindingConfig);
        } else {
            logger.warn("{} bindingConfig is NULL (item={}) -> processing bindingConfig aborted!", getBindingType(),
                    item);
        }
    }

    /**
     * This method parses the binding configuration string and transforms it to an {@link UrtsiItemConfiguration}.
     */
    protected void parseAndAddBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
        final Matcher matcher = CONFIG_BINDING_PATTERN.matcher(bindingConfig);

        if (!matcher.matches()) {
            bombOut(bindingConfig);
        }
        matcher.reset();
        if (matcher.find()) {
            int address = 1;
            int channel = 1;
            if (matcher.group(5) != null) { // both address and channel are specified
                channel = Integer.valueOf(matcher.group(5));
                if (matcher.group(2) == null) {
                    bombOut(bindingConfig);
                }
                address = Integer.valueOf(matcher.group(2));
            } else { // just channel specified
                if (matcher.group(2) == null) {
                    bombOut(bindingConfig);
                }
                channel = Integer.valueOf(matcher.group(2));
            }
            final UrtsiItemConfiguration urtsiConfig = new UrtsiItemConfiguration(matcher.group(1), channel, address);
            addBindingConfig(item, urtsiConfig);
        } else {
            bombOut(bindingConfig);
        }

    }

    /**
     * Shorthand for throwing lenghty exception
     */
    private void bombOut(String config) throws BindingConfigParseException {
        throw new BindingConfigParseException("bindingConfig '" + config
                + "' doesn't contain a valid Urtsii-binding-configuration. A valid configuration is matched by the RegExp '"
                + CONFIG_BINDING_PATTERN.pattern() + "'");
    }

    /**
     * Returns the device id which is associated to the given item.
     */
    @Override
    public String getDeviceId(String itemName) {
        UrtsiItemConfiguration config = getItemConfiguration(itemName);
        return config == null ? null : config.getDeviceId();
    }

    /**
     * Returns the channel which is associated to the given item.
     */
    @Override
    public int getChannel(String itemName) {
        UrtsiItemConfiguration config = getItemConfiguration(itemName);
        return config == null ? -1 : config.getChannel();
    }

    /**
     * Returns the urtsi device address which is associated to the given item.
     */
    @Override
    public int getAddress(String itemName) {
        UrtsiItemConfiguration config = getItemConfiguration(itemName);
        return config == null ? -1 : config.getAddress();
    }

    /**
     * Returns the item configuration for the given item.
     */
    private UrtsiItemConfiguration getItemConfiguration(String itemName) {
        return (UrtsiItemConfiguration) bindingConfigs.get(itemName);
    }
}
