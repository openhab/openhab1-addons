/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
