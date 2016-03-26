/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.state;

/**
 * A class to store the settings of an X10 device
 *
 * @author lolodomo
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
