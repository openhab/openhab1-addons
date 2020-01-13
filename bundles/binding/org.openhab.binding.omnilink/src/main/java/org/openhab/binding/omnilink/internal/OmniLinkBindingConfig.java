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
package org.openhab.binding.omnilink.internal;

import org.openhab.binding.omnilink.internal.model.OmnilinkDevice;
import org.openhab.core.binding.BindingConfig;

/**
 *
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkBindingConfig implements BindingConfig {

    private OmniLinkItemType objectType;
    private int number;
    private OmnilinkDevice device;

    /**
     * 
     * @param objectType that is linked to this item (units, thermostats, ect...)
     * @param number of the Omni object
     */
    public OmniLinkBindingConfig(OmniLinkItemType objectType, int number) {
        super();
        this.objectType = objectType;
        this.number = number;
    }

    /**
     * 
     * @return the Omni object type
     */
    public OmniLinkItemType getObjectType() {
        return objectType;
    }

    /**
     * 
     * @param objectType linked to this item
     */
    public void setObjectType(OmniLinkItemType objectType) {
        this.objectType = objectType;
    }

    /**
     * 
     * @return the omni object number
     */
    public int getNumber() {
        return number;
    }

    /**
     * 
     * @param number of the omni object number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 
     * @return the Omni device that this item is associated with
     */
    public OmnilinkDevice getDevice() {
        return device;
    }

    /**
     * 
     * @param device that this item is assoiciated with
     */
    public void setDevice(OmnilinkDevice device) {
        this.device = device;
    }

}
