/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.expire.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.expire.ExpireBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
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
 * @author Michael Wyraz
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

        super.processBindingConfiguration(context, item, bindingConfig);

        String[] durationAndState = bindingConfig.split(",", 2);

        ExpireBindingConfig config = new ExpireBindingConfig();
        config.expiresAfterAsText = durationAndState[0].trim();
        config.expiresAfterMs = parseDuration(durationAndState[0].trim());

        if (durationAndState.length > 1) {
            // use 2nd parameter as state. defaults to UNDEF
            String stateAsString = durationAndState[1].trim();
            config.expiredState = TypeParser.parseState(item.getAcceptedDataTypes(), stateAsString);
            if (config.expiredState == null) {
                throw new BindingConfigParseException("The string '" + stateAsString
                        + "' does not represent a valid state for item " + item.getName());
            }
        }

        logger.debug("Processed binding config for item {}, text='{}', ms={}, state='{}'", item.getName(),
                config.expiresAfterAsText, config.expiresAfterMs, config.expiredState);

        addBindingConfig(item, config);
    }

    protected static Pattern durationPattern = Pattern.compile("(?:([0-9]+)H)?\\s*(?:([0-9]+)M)?\\s*(?:([0-9]+)S)?",
            Pattern.CASE_INSENSITIVE);

    protected static long parseDuration(String duration) throws BindingConfigParseException {
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

    @Override
    public long getExpiresAfterMs(String itemName) {
        return getItemConfig(itemName).expiresAfterMs;
    }

    @Override
    public String getExpiresAfterAsText(String itemName) {
        return getItemConfig(itemName).expiresAfterAsText;
    }

    @Override
    public State getExpiredState(String itemName) {
        return getItemConfig(itemName).expiredState;
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
        /**
         * Human readable textual representation of duration (e.g. "13h 42m 12s")
         */
        protected String expiresAfterAsText;

        /**
         * duration in ms
         */
        protected long expiresAfterMs;

        /**
         * state to set if item is expired. Defaults to UNDEF
         */
        protected State expiredState = UnDefType.UNDEF;
    }
}
