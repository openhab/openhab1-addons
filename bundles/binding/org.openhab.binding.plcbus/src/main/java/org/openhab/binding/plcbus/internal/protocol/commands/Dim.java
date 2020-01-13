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
package org.openhab.binding.plcbus.internal.protocol.commands;

import org.openhab.binding.plcbus.internal.protocol.Command;
import org.openhab.binding.plcbus.internal.protocol.Convert;

/**
 * Dim Command in PLCBus Protocol
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class Dim extends Command {

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getId() {
        return 0x04;
    }

    /**
     * Faderate in Seconds
     * 
     * @return Faderate in Seconds
     */
    public int getSeconds() {
        return getData1();
    }

    /**
     * Sets the Faderate
     * 
     * @param seconds
     *            Faderate in Seconds
     */
    public void setSeconds(int seconds) {
        setData1(Convert.toByte(seconds));
    }

}
