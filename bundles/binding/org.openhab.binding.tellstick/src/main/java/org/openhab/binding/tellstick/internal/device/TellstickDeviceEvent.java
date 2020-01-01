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
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA.Method;

/**
 * A event received by callback and resent to listeners.
 *
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickDeviceEvent {

    private TellstickDevice device;
    private Method method; // Look in JNA -TELLSTICK_TURNON and below
    private String data;

    public TellstickDeviceEvent(TellstickDevice device, Method method, String data) {
        super();
        this.device = device;
        this.method = method;
        this.data = data;
    }

    public TellstickDevice getDevice() {
        return device;
    }

    public Method getMethod() {
        return method;
    }

    public String getData() {
        return data;
    }

}
