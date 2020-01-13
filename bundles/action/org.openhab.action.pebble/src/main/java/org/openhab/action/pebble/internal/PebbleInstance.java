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
package org.openhab.action.pebble.internal;

/**
 * Configuration for a Pebble instance.
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
public class PebbleInstance {

    private String name;

    private String token;

    public PebbleInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PebbleInstance) {
            return ((PebbleInstance) obj).getName().equals(getName());
        }

        return false;
    }

}
