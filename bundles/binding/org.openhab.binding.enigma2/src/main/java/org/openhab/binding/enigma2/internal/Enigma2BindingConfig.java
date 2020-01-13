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
package org.openhab.binding.enigma2.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Wrapper class for a Enigma2Binding Configuration
 *
 * @author Sebastian Kutschbach
 * @since 1.6.0
 *
 */
public class Enigma2BindingConfig implements BindingConfig {

    private Item item;
    private String deviceId;
    private Enigma2Command cmdId;
    private String cmdValue;

    public Enigma2BindingConfig(Item item, String deviceId, Enigma2Command cmdId, String cmdValue) {
        this.item = item;
        this.deviceId = deviceId;
        this.cmdId = cmdId;
        this.cmdValue = cmdValue;
    }

    public Item getItem() {
        return item;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Enigma2Command getCmdId() {
        return cmdId;
    }

    public String getCmdValue() {
        return cmdValue;
    }
}