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
package org.openhab.binding.nikobus.internal.core;

import org.openhab.binding.nikobus.internal.NikobusBinding;

/**
 * Basic interface to be implemented by all devices which want to listen to
 * commands received on the Nikobus.
 *
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface NikobusCommandListener {

    /**
     * Process a command received from the Nikobus. All commands are broadcasted
     * to all receivers, so it is up to the receiver to only act on the
     * appropriate commands.
     * 
     * @param command
     *            as it was read from serial port without CR.
     */
    public void processNikobusCommand(NikobusCommand command, NikobusBinding binding);

    /**
     * Get the name of the item.
     * 
     * @return item name
     */
    public String getName();
}
