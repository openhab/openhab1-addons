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
package org.openhab.binding.homematic.internal.communicator;

/**
 * CallbackReceiver defines those methods invoked by a Homematic server.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface HomematicCallbackReceiver {

    /**
     * Called when the Homematic server is sending a multicall message.
     */
    public void event(String interfaceId, String address, String parameterKey, Object value);

    /**
     * Called when the Homematic server detects a new device.
     */
    public void newDevices(String interfaceId, Object[] deviceDescriptions);

}
