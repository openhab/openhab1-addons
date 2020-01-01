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
package org.openhab.binding.fht.internal;

/**
 * This class repsents a waiting command to be send to a FHT-80b.
 *
 * @author Till Klocke
 * @since 1.4.0
 *
 */
public class FHTDesiredTemperatureCommand {

    private String address;
    private String command;

    public FHTDesiredTemperatureCommand(String address, String command) {
        this.address = address;
        this.command = command;
    }

    public String getAddress() {
        return address;
    }

    public String getCommand() {
        return command;
    }

}
