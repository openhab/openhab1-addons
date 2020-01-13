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
package org.openhab.binding.plcbus.internal;

import org.openhab.binding.plcbus.internal.protocol.PLCUnit;
import org.openhab.core.binding.BindingConfig;

/**
 * Configuration for a PLCBusBinding
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class PLCBusBindingConfig implements BindingConfig {

    private PLCUnit unit;
    private int seconds;

    PLCBusBindingConfig(String config) {
        seconds = 5;
        parse(config);
    }

    public PLCUnit getUnit() {
        return unit;
    }

    public int getSeconds() {
        return seconds;
    }

    private void parse(String config) {
        String[] parts = config.split(" ");

        if (parts.length >= 2) {
            unit = new PLCUnit(parts[0], parts[1]);

            if (parts.length >= 3) {
                seconds = Integer.parseInt(parts[2]);
            }
        }
    }

}