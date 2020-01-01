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
package org.openhab.binding.dmx.internal.cmd;

import org.openhab.binding.dmx.DmxService;

/**
 * DMX Command Interface. To be used for all different DMX commands.
 *
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxCommand {

    /**
     * List of available DMX Commands.
     */
    public static enum types {
        FADE,
        SFADE
    }

    /**
     * Execute a DMX command.
     * 
     * @param service
     *            on which to execute the command.
     */
    public void execute(DmxService service);

}
