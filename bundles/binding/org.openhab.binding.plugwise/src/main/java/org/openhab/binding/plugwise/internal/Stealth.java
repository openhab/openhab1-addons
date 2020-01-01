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
package org.openhab.binding.plugwise.internal;

/**
 * A class that represents a Plugwise Stealth device.
 *
 * The Stealth behaves like a Circle but it has a more compact form factor.
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class Stealth extends Circle {

    public Stealth(String mac, Stick stick, String name) {
        super(mac, stick, name);
        type = DeviceType.Stealth;
    }

}
