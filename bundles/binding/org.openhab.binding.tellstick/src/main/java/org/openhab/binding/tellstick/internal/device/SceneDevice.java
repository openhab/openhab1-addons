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
 * A Scene.
 *
 * @author jarlebh
 * @since 1.5.0
 */
public class SceneDevice extends TellstickDevice {

    public SceneDevice(int deviceId) throws SupportedMethodsException {
        super(deviceId);
    }

    /**
     * Executes Scene.
     * 
     * @throws TellstickException
     */
    public void execute() throws TellstickException {
        int status = JNA.CLibrary.INSTANCE.tdExecute(getId());
        if (status != TELLSTICK_SUCCESS) {
            throw new TellstickException(this, status);
        }
    }

    @Override
    public String getType() {
        return "Scene";
    }
}
