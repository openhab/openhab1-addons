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
package org.openhab.persistence.db4o.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

import com.db4o.config.Configuration;

/**
 * This is a Java bean used to persist item states with timestamps in the database.
 *
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class Db4oItem implements HistoricItem {

    private String name;
    private State state;
    private Date timestamp;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> " + state.toString();
    }

    static /* default */ void configure(Configuration config) {
        config.objectClass(Db4oItem.class).objectField("name").indexed(true);
        config.objectClass(Db4oItem.class).objectField("timestamp").indexed(true);

        config.objectClass(Db4oItem.class).cascadeOnUpdate(false);
        config.objectClass(Db4oItem.class).cascadeOnDelete(true);
    }

}
