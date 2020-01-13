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
package org.openhab.persistence.cosm.internal;

/**
 * This is a standard Java bean used as an input to the JSON serializer.
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class CosmEventBean {

    private String id;
    private String value;
    private String minValue = null;
    private String maxValue = null;

    public CosmEventBean(String feedId, String eventValue) {
        this.id = feedId;
        this.value = eventValue;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String eventValue) {
        this.value = eventValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

}
