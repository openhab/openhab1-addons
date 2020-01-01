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
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA;

/**
 * Up / Down devices can be such devices as Projector screens.
 *
 * @author jarlebh
 * @author peec
 * @since 1.5.0
 *
 */
public class UpDownDevice extends TellstickDevice {

    public UpDownDevice(int deviceId) throws SupportedMethodsException {
        super(deviceId);
    }

    /**
     * Sends up command.
     * 
     * @throws TellstickException
     */
    public void up() throws TellstickException {
        int status = JNA.CLibrary.INSTANCE.tdUp(getId());
        if (status != TELLSTICK_SUCCESS) {
            throw new TellstickException(this, status);
        }
    }

    /**
     * Sends down command.
     * 
     * @throws TellstickException
     */
    public void down() throws TellstickException {
        int status = JNA.CLibrary.INSTANCE.tdDown(getId());
        if (status != TELLSTICK_SUCCESS) {
            throw new TellstickException(this, status);
        }
    }

    /**
     * Stops execution.
     * 
     * @throws TellstickException
     */
    public void stop() throws TellstickException {
        int status = JNA.CLibrary.INSTANCE.tdStop(getId());
        if (status != TELLSTICK_SUCCESS) {
            throw new TellstickException(this, status);
        }
    }

    @Override
    public String getType() {
        return "Projector Screen";
    }

}
