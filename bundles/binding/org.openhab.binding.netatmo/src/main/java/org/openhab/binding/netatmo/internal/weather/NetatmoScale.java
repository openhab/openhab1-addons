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
package org.openhab.binding.netatmo.internal.weather;

import org.apache.commons.lang.StringUtils;

/**
 * @author Rob Nielsen
 * @since 1.8.0
 *
 *        This enum holds all the different scales for the Netatmo binding
 *        when using the Netatmo getMeasure api
 */
public enum NetatmoScale {
    MAX("max"),
    THIRTY_MIN("30min"),
    ONE_HOUR("1hour"),
    THREE_HOURS("3hours"),
    ONE_DAY("1day"),
    ONE_WEEK("1week"),
    ONE_MONTH("1month");

    String scale;

    private NetatmoScale(String scale) {
        this.scale = scale;
    }

    public String getScale() {
        return scale;
    }

    public static NetatmoScale fromString(String scale) {
        if (!StringUtils.isEmpty(scale)) {
            for (NetatmoScale unitSystemType : NetatmoScale.values()) {
                if (unitSystemType.getScale().equalsIgnoreCase(scale)) {
                    return unitSystemType;
                }
            }
        }
        throw new IllegalArgumentException("Invalid scale: " + scale);
    }
}