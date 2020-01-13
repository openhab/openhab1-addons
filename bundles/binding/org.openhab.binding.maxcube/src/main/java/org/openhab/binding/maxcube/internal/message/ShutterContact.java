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
package org.openhab.binding.maxcube.internal.message;

import org.openhab.core.library.types.OpenClosedType;

/**
 * MAX!Cube Shutter contact device.
 *
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class ShutterContact extends Device {

    private OpenClosedType shutterState = null;
    private boolean shutterStateUpdated = false;

    public ShutterContact(Configuration c) {
        super(c);
    }

    public void setShutterState(OpenClosedType shutterState) {
        if (this.shutterState != shutterState) {
            logger.debug("updated shutterstate from " + this.shutterState + " to " + shutterState);
            this.shutterStateUpdated = true;
        } else {
            this.shutterStateUpdated = false;
        }
        this.shutterState = shutterState;
    }

    public OpenClosedType getShutterState() {
        return shutterState;
    }

    public boolean isShutterStateUpdated() {
        return this.shutterStateUpdated;
    }

    @Override
    public DeviceType getType() {
        return DeviceType.ShutterContact;
    }
}
