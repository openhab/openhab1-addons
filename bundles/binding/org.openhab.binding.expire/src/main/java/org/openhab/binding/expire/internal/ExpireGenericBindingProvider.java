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
package org.openhab.binding.expire.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.expire.ExpireBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Michael Wyraz -- initial implementation
 * @author John Cocula -- added command support, tweaks
 * @since 1.9.0
 */
public class ExpireGenericBindingProvider extends AbstractGenericBindingProvider implements ExpireBindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(ExpireGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "expire";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // valid for any item type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        ExpireBindingConfig config = new ExpireBindingConfig(item, bindingConfig);

        logger.debug("Processed binding config for item {}: {}", item.getName(), config);

        super.processBindingConfiguration(context, item, bindingConfig);

        addBindingConfig(item, config);
    }

    @Override
    public long getDuration(String itemName) {
        return getItemConfig(itemName).duration;
    }

    @Override
    public String getDurationString(String itemName) {
        return getItemConfig(itemName).durationString;
    }

    @Override
    public State getExpireState(String itemName) {
        return getItemConfig(itemName).expireState;
    }

    @Override
    public Command getExpireCommand(String itemName) {
        return getItemConfig(itemName).expireCommand;
    }

    private ExpireBindingConfig getItemConfig(String itemName) {
        return (ExpireBindingConfig) bindingConfigs.get(itemName);
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Michael Wyraz
     * @since 1.9.0
     */
    private static class ExpireBindingConfig implements BindingConfig {

        protected static final Pattern durationPattern = Pattern
                .compile("(?:([0-9]+)H)?\\s*(?:([0-9]+)M)?\\s*(?:([0-9]+)S)?", Pattern.CASE_INSENSITIVE);
        protected static final String COMMAND_PREFIX = "command=";
        protected static final String STATE_PREFIX = "state=";

        /**
         * Human readable textual representation of duration (e.g. "13h 42m 12s")
         */
        protected String durationString;

        /**
         * duration in ms
         */
        protected long duration;

        /**
         * state to post if item is expired.
         */
        protected State expireState;

        /**
         * command to post if item is expired.
         */
        protected Command expireCommand;

        private long parseDuration(String duration) throws BindingConfigParseException {
            Matcher m = durationPattern.matcher(duration);
            if (!m.matches() || (m.group(1) == null && m.group(2) == null && m.group(3) == null)) {
                throw new BindingConfigParseException(
                        "Invalid duration: " + duration + ". Expected something like: '1h 15m 30s'");
            }

            long ms = 0;
            if (m.group(1) != null) {
                ms += Long.parseLong(m.group(1)) * 60 * 60 * 1000;
            }
            if (m.group(2) != null) {
                ms += Long.parseLong(m.group(2)) * 60 * 1000;
            }
            if (m.group(3) != null) {
                ms += Long.parseLong(m.group(3)) * 1000;
            }
            return ms;
        }

        /**
         * Construct an ExpireBindingConfig from the {@code bindingConfig} string.
         *
         * Valid syntax:
         *
         * {@code &lt;duration&gt;[,[state=|command=|]&lt;stateOrCommand&gt;]}<br>
         * if neither state= or command= is present, assume state
         *
         * @param item the item to which we are bound
         * @param bindingConfig the string that the user specified in the .items file
         * @throws BindingConfigParseException if it is ill-formatted
         */
        ExpireBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
            int commaPos = bindingConfig.indexOf(',');

            durationString = (commaPos >= 0) ? bindingConfig.substring(0, commaPos).trim() : bindingConfig.trim();
            duration = parseDuration(durationString);

            String stateOrCommand = ((commaPos >= 0) && (bindingConfig.length() - 1) > commaPos)
                    ? bindingConfig.substring(commaPos + 1).trim() : null;

            if ((stateOrCommand != null) && (stateOrCommand.length() > 0)) {
                if (stateOrCommand.startsWith(COMMAND_PREFIX)) {
                    String commandString = stateOrCommand.substring(COMMAND_PREFIX.length());
                    expireCommand = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandString);
                    if (expireCommand == null) {
                        throw new BindingConfigParseException("The string '" + commandString
                                + "' does not represent a valid command for item " + item.getName());
                    }
                } else {
                    if (stateOrCommand.startsWith(STATE_PREFIX)) {
                        stateOrCommand = stateOrCommand.substring(STATE_PREFIX.length());
                    }
                    String stateString = stateOrCommand;
                    expireState = TypeParser.parseState(item.getAcceptedDataTypes(), stateString);
                    if (expireState == null) {
                        throw new BindingConfigParseException("The string '" + stateString
                                + "' does not represent a valid state for item " + item.getName());
                    }
                }
            } else {
                // default is to post Undefined state
                expireCommand = null;
                expireState = UnDefType.UNDEF;
            }
        }

        @Override
        public String toString() {
            return "duration='" + durationString + "', ms=" + duration + ", state='" + expireState + "', command='"
                    + expireCommand + "'";
        }
    }
}
