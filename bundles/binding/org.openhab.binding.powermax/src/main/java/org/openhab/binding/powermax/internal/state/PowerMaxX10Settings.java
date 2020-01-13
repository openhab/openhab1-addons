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
package org.openhab.binding.powermax.internal.state;

/**
 * A class to store the settings of an X10 device
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxX10Settings {

    private String name;
    private boolean enabled;

    public PowerMaxX10Settings(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    /**
     * @return the name of the X10 device
     */
    public String getName() {
        return name;
    }

    /**
     * @return true if the X10 device is enabled; false if not
     */
    public boolean isEnabled() {
        return enabled;
    }

}
