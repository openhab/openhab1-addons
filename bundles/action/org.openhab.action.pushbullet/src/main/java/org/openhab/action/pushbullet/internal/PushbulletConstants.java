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
 * This class contains the common constants used in the PushbulletAPIConnector binding
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushbulletConstants {

    public static final String ACCESS_TOKEN_KEY = "accesstoken";

    public static final String BOTS_KEY = "bots";

    public static final String DEFAULT_BOTNAME = "DEFAULT";

    public static final String API_URL_PUSHES = "https://api.pushbullet.com/v2/pushes";

    public static final int TIMEOUT = 30 * 1000; // 30 seconds
}
