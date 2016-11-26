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

        Duration expiresAfter = parseDuration(bindingConfig);
        config.expiresAfterMs = expiresAfter.toMillis();
        config.expiresAfterAsText = formatDuration(expiresAfter);

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
        return ((ExpireBindingConfig) bindingConfigs.get(itemName)).expiresAfterMs;
    }

    @Override
    public String getExpiresAfterAsText(String itemName)
    {
        return ((ExpireBindingConfig) bindingConfigs.get(itemName)).expiresAfterAsText;
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
    }

}
