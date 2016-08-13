/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
