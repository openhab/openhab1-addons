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
package org.openhab.binding.zwave.internal.protocol.commandclass;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * Interface to implement for all command classes that implement the SET
 * commands like SET value.
 *
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public interface ZWaveSetCommands {
    /**
     * Gets a SerialMessage with the SET command
     *
     * @param value the value to set.
     * @return the serial message
     */
    public SerialMessage setValueMessage(int value);
}
