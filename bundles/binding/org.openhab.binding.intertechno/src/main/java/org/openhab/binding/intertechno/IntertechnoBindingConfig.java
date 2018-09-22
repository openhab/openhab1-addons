/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
