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
package org.openhab.persistence.influxdb.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * This is a Java bean used to return historic items from Influxdb.
 *
 * @author Theo Weiss - Initial Contribution
 * @since 1.5.0
 *
 */
public class InfluxdbItem implements HistoricItem {

    final private String name;
    final private State state;
    final private Date timestamp;

    public InfluxdbItem(String name, State state, Date timestamp) {
        this.name = name;
        this.state = state;
        this.timestamp = timestamp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> " + state.toString();
    }

}
