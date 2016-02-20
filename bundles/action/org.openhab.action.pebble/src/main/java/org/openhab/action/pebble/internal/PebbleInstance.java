/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
