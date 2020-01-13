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
package org.openhab.action.pushbullet.internal;

/**
 * This class is the model for a PushbulletAPIConnector bot, identified by its name and access token.
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushbulletBot {

    private String name;

    private String token;

    public PushbulletBot(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "PushbulletBot {" + "name='" + name + '\'' + ", token='" + token + '\'' + '}';
    }
}
