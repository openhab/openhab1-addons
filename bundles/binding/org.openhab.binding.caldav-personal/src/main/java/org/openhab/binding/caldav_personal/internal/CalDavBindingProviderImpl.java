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
package org.openhab.binding.caldav_personal.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.openhab.binding.caldav_personal.CalDavBindingProvider;
import org.openhab.binding.caldav_personal.internal.CalDavConfig.Type;
import org.openhab.binding.caldav_personal.internal.CalDavConfig.Value;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * caldavPresence="calendar:my type:presence"
 * caldavPresence="calendar:my type:event eventNr:0 value:name"
 * caldavPresence="calendar:my type:active eventNr:0 value:name filter-name:'.*Biotonne.*'"
 * caldavPresence="calendar:my type:active eventNr:0 value:name filter-category:'Arbeit'"
 * caldavPresence="calendar:my type:upcoming eventNr:0 value:place"
 *
 * valid values: name, description, place, start, end
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavBindingProviderImpl extends AbstractGenericBindingProvider implements CalDavBindingProvider {
    private static final Logger logger = LoggerFactory.getLogger(CalDavBindingProviderImpl.class);

    private static final String REGEX_CALENDAR = "calendar:'?([A-Za-z-_]+(, ?[A-Za-z-_]+)*)'?";
    private static final String REGEX_TYPE = "type:'?([A-Za-z]+)'?";
    private static final String REGEX_EVENT_NR = "eventNr:'?([0-9]+)'?";
    private static final String REGEX_VALUE = "value:'?([A-Za-z]+)'?";
    private static final String REGEX_FILTER_NAME = "filter-name:'?([A-Za-z\\.\\*\\+\\- \\|]+)'?";
    private static final String REGEX_FILTER_CATEGORY = "filter-category:'?([A-Za-z-_]+(, ?[A-Za-z-_]+)*)'?";
    private static final String REGEX_FILTER_CATEGORY_ANY = "filter-category-any:'?([A-Za-z-_]+(, ?[A-Za-z-_]+)*)'?";

    private boolean categoriesFiltersAny = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "caldavPersonal";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof DateTimeItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch-, String and DateTimeItem are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig == null) {
            logger.debug("binding-configuration is currently not set for item: " + item.getName());
            return;
        }

        logger.trace("handling config: {}", bindingConfig);

        // calendar
        String calendarString = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_CALENDAR);
        List<String> calendar = new ArrayList<String>();
        if (calendarString != null) {
            for (String str : calendarString.split(",")) {
                calendar.add(str.trim());
            }
        }

        String type = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_TYPE);
        Type typeEnum = null;
        String eventNr = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_EVENT_NR);
        String value = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_VALUE);
        Value valueEnum = null;
        String filterName = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_FILTER_NAME);
        String filterCategoryString = CalDavBindingProviderImpl.getConfigValue(bindingConfig, REGEX_FILTER_CATEGORY);
        String filterCategoryAnyString = CalDavBindingProviderImpl.getConfigValue(bindingConfig,
                REGEX_FILTER_CATEGORY_ANY);
        List<String> filterCategory = new ArrayList<String>();

        if (filterCategoryString != null && filterCategoryAnyString != null) {
            throw new BindingConfigParseException(
                    bindingConfig + "filter-category and filter-category-any are mutually exclusive");
        }

        if (filterCategoryString != null) {
            for (String str : filterCategoryString.split(",")) {
                filterCategory.add(str.trim());
            }
        }

        if (filterCategoryAnyString != null) {
            for (String str : filterCategoryAnyString.split(",")) {
                filterCategory.add(str.trim());
                categoriesFiltersAny = true;
            }
        }

        logger.trace("found values: calendar={}, type={}, eventNr={}, value={}, filterName={}, filterCategory={}",
                calendar, type, eventNr, value, filterName, filterCategory);

        if (calendar == null || calendar.size() == 0) {
            throw new BindingConfigParseException("missing attribute 'calendar'");
        }
        if (type == null) {
            throw new BindingConfigParseException("missing attribute 'type'");
        }

        try {
            typeEnum = CalDavConfig.Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BindingConfigParseException("type '" + type + "' not valid");
        }

        if (typeEnum != Type.PRESENCE) {
            if (eventNr == null) {
                throw new BindingConfigParseException("missing attribute 'eventNr'");
            }
            if (value == null) {
                throw new BindingConfigParseException("missing attribute 'value'");
            }
            if (!NumberUtils.isDigits(eventNr)) {
                throw new BindingConfigParseException("attribute 'eventNr' must be a valid integer");
            }
            try {
                valueEnum = CalDavConfig.Value.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BindingConfigParseException("value '" + type + "' not valid");
            }
        } else {
            if (eventNr != null) {
                throw new BindingConfigParseException("not required attribute 'eventNr'");
            }
            if (value != null) {
                throw new BindingConfigParseException("not required attribute 'value'");
            }
        }

        logger.debug("adding item: {}", item.getName());
        if (categoriesFiltersAny == false) {
            this.addBindingConfig(item, new CalDavConfig(calendar, typeEnum,
                    NumberUtils.toInt(eventNr == null ? "0" : eventNr), valueEnum, filterName, filterCategory));
        } else {
            this.addBindingConfig(item,
                    new CalDavConfig(calendar, typeEnum, NumberUtils.toInt(eventNr == null ? "0" : eventNr), valueEnum,
                            filterName, filterCategory, categoriesFiltersAny));
        }
    }

    public static String getConfigValue(String input, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    @Override
    public CalDavConfig getConfig(String item) {
        return (CalDavConfig) this.bindingConfigs.get(item);
    }
}
