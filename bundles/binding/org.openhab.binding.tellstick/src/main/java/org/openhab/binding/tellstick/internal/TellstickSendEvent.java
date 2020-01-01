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
package org.openhab.binding.tellstick.internal;

import org.openhab.binding.tellstick.TellstickBindingConfig;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.core.types.Command;

/**
 * This is a wrapper class for handle the send queue to the Tellstick device.
 *
 * @author Elias Gabrielsson
 * @since 1.9.0
 */

public class TellstickSendEvent implements Comparable<TellstickSendEvent> {
    private TellstickBindingConfig config;
    private TellstickDevice dev;
    private Command command;
    private Long eventTime;

    public TellstickSendEvent(TellstickBindingConfig config, TellstickDevice dev, Command command, Long eventTime) {
        this.config = config;
        this.dev = dev;
        this.command = command;
        this.eventTime = eventTime;
    }

    public Command getCommand() {
        return command;
    }

    public TellstickBindingConfig getConfig() {
        return config;
    }

    public TellstickDevice getDev() {
        return dev;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setConfig(TellstickBindingConfig config) {
        this.config = config;
    }

    public void setDev(TellstickDevice dev) {
        this.dev = dev;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int compareTo(TellstickSendEvent o) {
        return eventTime.compareTo(o.getEventTime());
    }
}
