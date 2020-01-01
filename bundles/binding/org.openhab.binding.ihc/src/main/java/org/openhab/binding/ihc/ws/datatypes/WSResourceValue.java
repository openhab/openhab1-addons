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
package org.openhab.binding.ihc.ws.datatypes;

/**
 * <p>
 * Java class for WSResourceValue complex type.
 *
 */

public class WSResourceValue {

    protected int resourceID;

    WSResourceValue() {

    }

    WSResourceValue(int resourceID) {
        this.resourceID = resourceID;
    }

    /**
     * Gets the value of the resource ID property.
     * 
     */
    public int getResourceID() {
        return resourceID;
    }

    /**
     * Sets the value of the resource ID property.
     * 
     */
    public void setResourceID(int value) {
        this.resourceID = value;
    }
}
