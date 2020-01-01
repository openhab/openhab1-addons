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
package org.openhab.binding.squeezebox;

import org.openhab.binding.squeezebox.internal.CommandType;
import org.openhab.core.binding.BindingConfig;

public class SqueezeboxBindingConfig implements BindingConfig {
    private final String playerId;
    private final CommandType commandType;
    private final String extra;

    public SqueezeboxBindingConfig(String playerId, CommandType commandType, String extra) {
        this.playerId = playerId;
        this.commandType = commandType;
        this.extra = extra;
    }

    public String getPlayerId() {
        return playerId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getExtra() {
        return extra;
    }
}
