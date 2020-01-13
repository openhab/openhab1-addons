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
package org.openhab.binding.withings.internal.model;

/**
 * Java object for response of Withings API.
 *
 * @see http://www.withings.com/de/api#bodymetrics
 * @author Dennis Nobel
 * @since 1.5.0
 */
public enum Category {
    MEASURE("Measure", 1),
    TARGET("Target", 2);

    public static Category getForType(int type) {
        Category[] categories = values();
        for (Category category : categories) {
            if (category.type == type) {
                return category;
            }
        }
        return null;
    }

    public String description;

    private int type;

    private Category(String description, int type) {
        this.description = description;
        this.type = type;
    }
}
