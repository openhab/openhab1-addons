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
package org.openhab.binding.intertechno;

import org.openhab.core.binding.BindingConfig;

/**
 * This class represents the configuration of a receiving intertechno device. An
 * address and the commands for on and off need to be configured here.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class IntertechnoBindingConfig implements BindingConfig {

    private String commandOn;
    private String commandOff;

    public IntertechnoBindingConfig(String commandOn, String commandOff) {
        this.commandOn = commandOn;
        this.commandOff = commandOff;
    }

    public String getCommandON() {
        return commandOn;
    }

    public String getCommandOFF() {
        return commandOff;
    }

}
