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
package org.openhab.binding.denon.internal;

/**
 * Callback interface to signal that new information was received.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public interface DenonUpdateReceivedCallback {

    /**
     * Update was received.
     * 
     * @param command
     *            The line of text that was received from the telnet connection
     */
    public void updateReceived(String command);

    /**
     * The listener has successfully connected to the receiver.
     */
    public void listenerConnected();

    /**
     * The listener has lost connection to the receiver.
     */
    public void listenerDisconnected();
}
