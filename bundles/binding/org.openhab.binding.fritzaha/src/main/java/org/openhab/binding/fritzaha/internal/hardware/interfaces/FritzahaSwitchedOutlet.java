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
package org.openhab.binding.fritzaha.internal.hardware.interfaces;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;

/**
 * Interface for handling switched outlets.
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaSwitchedOutlet extends FritzahaDevice {
    /**
     * Sends command to toggle the switch. Should be implemented asynchronously
     * if possible.
     * 
     * @param onOff
     *            State in which to set the switch
     * @param webIface
     *            Web interface to use
     */
    public void setSwitchState(boolean onOff, String itemName, FritzahaWebInterface webIface);

    /**
     * Inquires about the switch state and updates item state accordingly.
     * Should be asynchronous if possible.
     * 
     * @param itemName
     *            Item to update
     * @param webIface
     *            Web interface to use
     */
    public void updateSwitchState(String itemName, FritzahaWebInterface webIface);
}
