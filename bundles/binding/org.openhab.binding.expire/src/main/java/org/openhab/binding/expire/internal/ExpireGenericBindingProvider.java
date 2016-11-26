/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.expire.internal;

import java.time.Duration;
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

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Michael Wyraz
 * @since 1.9.0
 */
public class ExpireGenericBindingProvider extends AbstractGenericBindingProvider implements ExpireBindingProvider
{

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType()
    {
        return "expire";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException
    {
        // valid for any item type
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException
    {
        super.processBindingConfiguration(context, item, bindingConfig);
        ExpireBindingConfig config = new ExpireBindingConfig();

        String[] durationAndState = bindingConfig.split(",", 2);

        Duration expiresAfter = parseDuration(durationAndState[0].trim());
        config.expiresAfterMs = expiresAfter.toMillis();
        config.expiresAfterAsText = formatDuration(expiresAfter);

        if (durationAndState.length > 1)
        {
            // use 2nd parameter as state. defaults to UNDEF
            config.expiredState = TypeParser.parseState(item.getAcceptedDataTypes(), durationAndState[1].trim());
        }

        addBindingConfig(item, config);
    }

    protected static Pattern durationPattern = Pattern.compile("(\\d+[hH])?\\s*(\\d+[mM])?\\s*(\\d+[sS])?");

    protected static String formatDuration(Duration duration)
    {
        if (duration == null)
        {
            return null;
        }
        // PT27H46M40S -> 27h 46m 40s
        return duration.toString().replaceFirst("^PT", "").replaceAll("([HMS])", "$1 ").toLowerCase();
    }

    protected static Duration parseDuration(String duration) throws BindingConfigParseException
    {
        Matcher m = durationPattern.matcher(duration);
        if (!m.matches() || (m.group(1) == null && m.group(2) == null) && m.group(3) == null)
        {
            throw new BindingConfigParseException("Invalid duration: " + duration + ". Expected something like: '1h 15m 30s'");
        }
        StringBuilder sb = new StringBuilder("PT");
        for (int i = 1; i <= 3; i++)
        {
            if (m.group(i) != null)
            {
                sb.append(m.group(i));
            }
        }
        return Duration.parse(sb);
    }

    @Override
    public long getExpiresAfterMs(String itemName)
    {
        return getItemConfig(itemName).expiresAfterMs;
    }

    @Override
    public String getExpiresAfterAsText(String itemName)
    {
        return getItemConfig(itemName).expiresAfterAsText;
    }

    @Override
    public State getExpiredState(String itemName)
    {
        return getItemConfig(itemName).expiredState;
    }

    private ExpireBindingConfig getItemConfig(String itemName)
    {
        return (ExpireBindingConfig) bindingConfigs.get(itemName);
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Michael Wyraz
     * @since 1.9.0
     */
    class ExpireBindingConfig implements BindingConfig
    {
        /**
         * Human readable textual representation if duration (e.g. "13h 42m 12s")
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
