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

import org.openhab.core.binding.BindingConfig;

/**
 * Configuration class
 *
 * @author Robert Delbrück
 * @since 1.8.0
 *
 */
public class CalDavConfig implements BindingConfig {
    public static enum Type {
        PRESENCE,
        EVENT,
        ACTIVE,
        UPCOMING
    }

    public static enum Value {
        NAME,
        DESCRIPTION,
        PLACE,
        START,
        END,
        TIME,
        NAMEANDTIME
    }

    private final List<String> calendar;
    private final Type type;
    private final int eventNr;
    private final Value value;
    private final String filterName;
    private final List<String> filterCategory = new ArrayList<String>();
    private final boolean categoriesFiltersAny;

    public CalDavConfig(List<String> calendar, Type type, int eventNr, Value value, String filterName,
            List<String> filterCategory) {
        this.calendar = calendar;
        this.type = type;
        this.eventNr = eventNr;
        this.value = value;
        this.filterName = filterName;
        this.filterCategory.addAll(filterCategory);
        this.categoriesFiltersAny = false;
    }

    public CalDavConfig(List<String> calendar, Type type, int eventNr, Value value, String filterName,
            List<String> filterCategory, boolean categoriesFiltersAny) {
        this.calendar = calendar;
        this.type = type;
        this.eventNr = eventNr;
        this.value = value;
        this.filterName = filterName;
        this.filterCategory.addAll(filterCategory);
        this.categoriesFiltersAny = categoriesFiltersAny;
    }

    public boolean getCategoriesFiltersAny() {
        return categoriesFiltersAny;
    }

    public List<String> getCalendar() {
        return calendar;
    }

    public Type getType() {
        return type;
    }

    public int getEventNr() {
        return eventNr;
    }

    public Value getValue() {
        return value;
    }

    public String getFilterName() {
        return filterName;
    }

    public List<String> getFilterCategory() {
        return filterCategory;
    }

    @Override
    public String toString() {
        return "CalDavPresenceConfig [calendar=" + calendar + ", type=" + type + ", eventNr=" + eventNr + ", value="
                + value + ", filterName=" + filterName + ", categoriesFiltersAny=" + categoriesFiltersAny + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calendar == null) ? 0 : calendar.hashCode());
        result = prime * result + eventNr;
        result = prime * result + ((filterCategory == null) ? 0 : filterCategory.hashCode());
        result = prime * result + ((filterName == null) ? 0 : filterName.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        result = prime * result + (categoriesFiltersAny ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CalDavConfig other = (CalDavConfig) obj;
        if (calendar == null) {
            if (other.calendar != null) {
                return false;
            }
        } else if (!calendar.equals(other.calendar)) {
            return false;
        }
        if (eventNr != other.eventNr) {
            return false;
        }
        if (filterCategory == null) {
            if (other.filterCategory != null) {
                return false;
            }
        } else if (!filterCategory.equals(other.filterCategory)) {
            return false;
        }
        if (filterName == null) {
            if (other.filterName != null) {
                return false;
            }
        } else if (!filterName.equals(other.filterName)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (value != other.value) {
            return false;
        }
        if (categoriesFiltersAny != other.categoriesFiltersAny) {
            return false;
        }
        return true;
    }

    public int getUniqueEventListKey() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calendar == null) ? 0 : calendar.hashCode());
        result = prime * result + ((filterCategory == null) ? 0 : filterCategory.hashCode());
        result = prime * result + ((filterName == null) ? 0 : filterName.hashCode());
        result = prime * result + (categoriesFiltersAny ? 1231 : 1237);
        return result;
    }

}
