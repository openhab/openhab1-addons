/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.girahomeserver.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.girahomeserver.GiraHomeServerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Jochen Mattes
 * @since 1.9.0
 */
public class GiraHomeServerGenericBindingProvider extends AbstractGenericBindingProvider
        implements GiraHomeServerBindingProvider {

    /**
     * This is an internal data structure to store information from the binding
     * config strings and use it to answer the requests to the girahomeserver
     * binding provider.
     */
    static class GiraHomeServerBindingConfig extends HashMap<Command, GiraHomeServerBindingConfigElement>
            implements BindingConfig {

        /** generated serialVersion UID */
        private static final long serialVersionUID = -8421879251490268240L;

    }

    /**
     * This is an internal data structure to store information from the binding
     * config strings and use it to answer the requests to the girahomeserver binding
     * provider.
     */
    static class GiraHomeServerBindingConfigElement implements BindingConfig {

        public String communicationObject = null;
        int refreshInterval = 0;
        String transformation = null;

        @Override
        public String toString() {
            return "ExecBindingConfigElement [co=" + communicationObject + ", refreshInterval=" + refreshInterval
                    + ", transformation=" + transformation + "]";
        }

    }

    /** {@link Pattern} which matches a binding configuration part */
    private static final Pattern BASE_CONFIG_PATTERN = Pattern.compile("(<|>|)([0-9/]+)(\\s|$)");

    protected static final Command BIDIRECTIONAL_BINDING_KEY = StringType.valueOf("BIDIRECTIONAL_BINDING");

    /**
     * Artificial command for the exec-in configuration (which has no command
     * part by definition). Because we use this artificial command we can reuse
     * the {@link ExecBindingConfig} for both in- and out-configuration.
     */
    protected static final Command IN_BINDING_KEY = StringType.valueOf("IN_BINDING");

    protected static final Command OUT_BINDING_KEY = StringType.valueOf("OUT_BINDING");

    protected GiraHomeServerBindingConfig createBindingConfig(Item item, Command command, String bindingConfig,
            GiraHomeServerBindingConfig config) throws BindingConfigParseException {

        GiraHomeServerBindingConfigElement configElement;
        configElement = new GiraHomeServerBindingConfigElement();
        configElement.communicationObject = bindingConfig;
        config.put(command, configElement);

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "girahomeserver";
    }

    /**
     * get the communication object for inbound updates
     *
     * @param itemName
     * @param value
     * @return
     */
    @Override
    public String getOutboundCommunicationObject(String itemName, Type value) {
        return getCommunicationObject(OUT_BINDING_KEY, itemName, value);
    }

    /**
     * get the revelant communication object
     *
     * @param direction
     * @param itemName
     * @param value
     * @return
     */
    private String getCommunicationObject(Command direction, String itemName, Type value) {
        try {
            GiraHomeServerBindingConfig config = (GiraHomeServerBindingConfig) bindingConfigs.get(itemName);
            if (config == null) {
                return null;
            }

            if (config.get(direction) != null) {
                return config.get(direction).communicationObject;
            }

            // fallback
            if (config.get(BIDIRECTIONAL_BINDING_KEY) != null) {
                return config.get(BIDIRECTIONAL_BINDING_KEY).communicationObject;
            }

            // no item found
            return null;

        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public String[] getItemNames(String communicationObject) {
        Set<String> itemNames = new HashSet<String>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            GiraHomeServerBindingConfig config = (GiraHomeServerBindingConfig) entry.getValue();
            if (config.get(BIDIRECTIONAL_BINDING_KEY).communicationObject.equals(communicationObject)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        super.processBindingConfiguration(context, item, bindingConfig);
        GiraHomeServerBindingConfig config = new GiraHomeServerBindingConfig();

        // test pattern
        Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);
        if (!matcher.matches()) {
            throw new BindingConfigParseException("Binding configuration has invalid format ");

        } else {
            matcher.reset();

            // extract configurations
            while (matcher.find()) {
                String direction = matcher.group(1);
                String bindingConfigPart = matcher.group(2);

                // parse command
                Command command;
                switch (direction) {
                    case "<":
                        command = IN_BINDING_KEY;
                    case ">":
                        command = OUT_BINDING_KEY;
                    default:
                        command = BIDIRECTIONAL_BINDING_KEY;
                }

                // parse item
                config = createBindingConfig(item, command, bindingConfigPart, config);
            }
        }
        addBindingConfig(item, config);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // al types of items are accepted
    }

}
